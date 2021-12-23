package smarticulous;

import smarticulous.db.Exercise;
import smarticulous.db.Submission;
import smarticulous.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Smarticulous {

    /**
     * The connection to the underlying DB.
     * <p>
     * null if the db has not yet been opened.
     */
    Connection db;
    /** magic number to convert int to date =
     millisec * sec * min * hours
     1000 * 60 * 60 * 24 = 86400000 */
    public static final long INT_TO_DATE=86400000L;

    /**
     * Open the smarticulous.Smarticulous SQLite database.
     * <p>
     * This should open the database, creating a new one if necessary, and set the {@link #db} field
     * to the new connection.
     * <p>
     * The open method should make sure the database contains the following tables, creating them if necessary:
     * <p>
     * - A ``User`` table containing the following columns (with their types):
     * <p>
     * =========  =====================
     * Column          Type
     * =========  =====================
     * UserId     Integer (Primary Key)
     * Username   Text
     * Firstname  Text
     * Lastname   Text
     * Password   Text
     * =========  =====================
     * <p>
     * - An ``smarticulous.db.Exercise`` table containing the following columns:
     * <p>
     * ============  =====================
     * Column          Type
     * ============  =====================
     * ExerciseId    Integer (Primary Key)
     * Name          Text
     * DueDate       Integer
     * ============  =====================
     * <p>
     * - A ``Question`` table containing the following columns:
     * <p>
     * ============  =====================
     * Column          Type
     * ============  =====================
     * ExerciseId     Integer
     * QuestionId     Integer
     * Name           Text
     * Desc           Text
     * Points         Integer
     * ============  =====================
     * <p>
     * In this table the combination of ``ExerciseId``,``QuestionId`` together comprise the primary key.
     * <p>
     * - A ``smarticulous.db.Submission`` table containing the following columns:
     * <p>
     * ===============  =====================
     * Column             Type
     * ===============  =====================
     * SubmissionId      Integer (Primary Key)
     * UserId           Integer
     * ExerciseId        Integer
     * SubmissionTime    Integer
     * ===============  =====================
     * <p>
     * - A ``QuestionGrade`` table containing the following columns:
     * <p>
     * ===============  =====================
     * Column             Type
     * ===============  =====================
     * SubmissionId      Integer
     * QuestionId        Integer
     * Grade            Real
     * ===============  =====================
     * <p>
     * In this table the combination of ``SubmissionId``,``QuestionId`` together comprise the primary key.
     *
     * @param dburl The JDBC url of the database to open (will be of the form "jdbc:sqlite:...")
     * @return the new connection
     */
    public Connection openDB(String dburl) throws SQLException {
        try {
            db = DriverManager.getConnection(dburl);
            Statement stmt = db.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS `User` ( `UserId` INTEGER PRIMARY KEY, `Username` TEXT UNIQUE , `Firstname` TEXT, `Lastname` TEXT, `Password` TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS `Exercise` ( `ExerciseId` INTEGER PRIMARY KEY, `Name` TEXT  , `DueDate` INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS `Question` ( `ExerciseId` INTEGER , `QuestionId` INTEGER  , `Name` TEXT, `Desc` TEXT, `Points` INTEGER, CONSTRAINT PK_Question PRIMARY KEY (ExerciseId,QuestionId))");
            stmt.execute("CREATE TABLE IF NOT EXISTS `Submission` ( `SubmissionId` INTEGER PRIMARY KEY, `UserId` INTEGER  , `ExerciseId` INTEGER, `SubmissionTime` INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS `QuestionGrade` ( `SubmissionId` INTEGER , `QuestionId` INTEGER  , `Grade` REAL, CONSTRAINT PK_QuestionGrade PRIMARY KEY (SubmissionId,QuestionId))");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Close the DB if it is open.
     */
    public void closeDB() throws SQLException {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    // =========== User Management =============

    /**
     * Add a user to the database / modify an existing user.
     * <p>
     * Add the user to the database if they don't exist. If a user with user.username does exist,
     * update their password and firstname/lastname in the database.
     *
     * @param user
     * @return the userid.
     */
    public int addOrUpdateUser(User user, String password) throws SQLException {
        int id = -1;
        try {
            /*Add a user to the database or  modify an existing user SQL query*/
            PreparedStatement update_or_add = db.prepareStatement("INSERT INTO User (Username,Firstname,Lastname,Password) VALUES (?,?,?,?)  " +
                    "ON CONFLICT(Username) DO UPDATE SET Firstname = ?, Lastname = ? , Password = ? WHERE Username = ?  " +
                    " ");
            /*Add too the PreparedStatement values */
            update_or_add.setString(1,user.username);
            update_or_add.setString(2,user.firstname);
            update_or_add.setString(3,user.lastname);
            update_or_add.setString(4,password);
            update_or_add.setString(5,user.firstname);
            update_or_add.setString(6,user.lastname);
            update_or_add.setString(7,password);
            update_or_add.setString(8,user.username);
            update_or_add.executeUpdate();

            /*send a query to get the user id */
            PreparedStatement get_userid_query = db.prepareStatement("SELECT UserId FROM User WHERE Username = ?");
            get_userid_query.setString(1,user.username);
            ResultSet numofmatch = get_userid_query.executeQuery();
            if(numofmatch.next()){
                id = numofmatch.getInt("UserId");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        // TODO: Implement
        return id;
    }


    /**
     * Verify a user's login credentials.
     *
     * @param username
     * @param password
     * @return true if the user exists in the database and the password matches; false otherwise.
     * <p>
     * Note: this is totally insecure. For real-life password checking, it's important to store only
     * a password hash
     * @see <a href="https://crackstation.net/hashing-security.htm">How to Hash Passwords Properly</a>
     */
    public boolean verifyLogin(String username, String password) throws SQLException {
        try {
            PreparedStatement verify_login = db.prepareStatement("SELECT UserId FROM User WHERE Username = ? AND Password = ?");
            verify_login.setString(1, username);
            verify_login.setString(2, password);
            ResultSet numofmatch = verify_login.executeQuery();
            if (numofmatch.next()) {
                return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // =========== Exercise Management =============

    /**
     * Add an exercise to the database.
     *
     * @param exercise
     * @return the new exercise id, or -1 if an exercise with this id already existed in the database.
     */
    public int addExercise(Exercise exercise) throws SQLException {
        int id = -1;
        try {
            /*send a query to check if  an exercise with this id already existed in the database */
            PreparedStatement check_id = db.prepareStatement("SELECT ExerciseId FROM Exercise  WHERE ExerciseId = ?");
            check_id.setInt(1,exercise.id);
            ResultSet numOfMatch = check_id.executeQuery();
            if(numOfMatch.next()){
                /*there is a exercise with this id already existed in the database , so return -1 */
                return -1;
            }
            else{
                /*insert new exercise to DB */
                PreparedStatement to_insert_query = db.prepareStatement("INSERT INTO Exercise (ExerciseId,Name,DueDate) VALUES (?,?,?)");
                to_insert_query.setInt(1,exercise.id);
                to_insert_query.setString(2,exercise.name);
                to_insert_query.setLong(3,(exercise.dueDate.getTime()));
                to_insert_query.executeUpdate();

                /*insert exercise's questions to DB */
                int i = 1;
                for(Exercise.Question que : exercise.questions){
                    PreparedStatement insert_question_query = db.prepareStatement("INSERT INTO Question (ExerciseId,QuestionId,Name,Desc,Points) VALUES (?,?,?,?,?)");
                    insert_question_query.setInt(1,exercise.id);
                    insert_question_query.setInt(2,i++);
                    insert_question_query.setString(3,que.name);
                    insert_question_query.setString(4,que.desc);
                    insert_question_query.setInt(5,que.points);
                    insert_question_query.executeUpdate();
                }
                return exercise.id;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        // TODO: Implement
        return -1;
    }


    /**
     * Return a list of all the exercises in the database.
     * <p>
     * The list should be sorted by exercise id.
     *
     * @return
     */
    public List<Exercise> loadExercises() throws SQLException {
        /*init List of exercise to return */
        List<Exercise> list_exercises = new ArrayList<>();
        try {
            Statement check_id = db.createStatement();
            ResultSet numOfMatch = check_id.executeQuery("SELECT * FROM Exercise ORDER BY ExerciseId");
            /*for every exercise, create a new exercise object and insert all the questions to the object
            * then add the exercise object to the list  */
            while (numOfMatch.next()){
                Exercise ex = new Exercise(numOfMatch.getInt("ExerciseId"),numOfMatch.getString("Name"),new Date(numOfMatch.getInt("DueDate")));
                PreparedStatement question_query = db.prepareStatement("SELECT * FROM Question Where ExerciseId = ?");
                question_query.setInt(1,numOfMatch.getInt("ExerciseId"));
                ResultSet numOfMatchQuestions = question_query.executeQuery();
                while (numOfMatchQuestions.next()){
                    ex.addQuestion(numOfMatchQuestions.getString("Name"),numOfMatchQuestions.getString("Desc"),numOfMatchQuestions.getInt("Points"));
                }
                list_exercises.add(ex);

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        // TODO: Implement
        return list_exercises;
    }

    // ========== Submission Storage ===============

    /**
     * Store a submission in the database.
     * The id field of the submission will be ignored if it is -1.
     * <p>
     * Return -1 if the corresponding user doesn't exist in the database.
     *
     * @param submission
     * @return the submission id.
     */
    public int storeSubmission(Submission submission) throws SQLException {
        // TODO: Implement
        try{
            /*check if the user is exists in the DB */
            PreparedStatement check_user = db.prepareStatement("SELECT UserId FROM User WHERE Username = ?");
            check_user.setString(1,submission.user.username);
            ResultSet numOfMatch = check_user.executeQuery();
            if(numOfMatch.next()){
                /*check if the submission.id == 1
                * if so insert a submission with out id ( the DB will insert one)
                * if not insert a submission with the given id */
                if(submission.id != -1){
                    PreparedStatement insert_submission_query = db.prepareStatement("INSERT INTO Submission  (SubmissionId,UserId,ExerciseId,SubmissionTime) VALUES (?,?,?,?)");
                    insert_submission_query.setInt(1,submission.id);
                    insert_submission_query.setInt(2,numOfMatch.getInt("UserId"));
                    insert_submission_query.setInt(3,submission.exercise.id);
                    insert_submission_query.setLong(3, (submission.submissionTime.getTime()));
                    insert_submission_query.executeUpdate();
                }
                else {
                    PreparedStatement insert_submission_query = db.prepareStatement("INSERT INTO Submission  (UserId,ExerciseId,SubmissionTime) VALUES (?,?,?)");
                    insert_submission_query.setInt(1, numOfMatch.getInt("UserId"));
                    insert_submission_query.setInt(2, submission.exercise.id);
                    insert_submission_query.setLong(3, (submission.submissionTime.getTime()));
                    insert_submission_query.executeUpdate();
                }
                /*add exercise to the DB if it is not already in  */
                addExercise(submission.exercise);

                /*send a query to get the submission id we just inserted  */
                PreparedStatement check_sub_id = db.prepareStatement("SELECT last_insert_rowid()");
                ResultSet numOfMatch_id = check_sub_id.executeQuery();
                if(numOfMatch_id.next()) {
                    /*for every question in the exercise of the submission,
                    * insert question grade to the DB  */
                    int i = 1;
                    for( float question_grade : submission.questionGrades){
                        PreparedStatement insert_question_query = db.prepareStatement("INSERT INTO QuestionGrade  (SubmissionId,QuestionId,Grade) VALUES (?,?,?)");
                        insert_question_query.setInt(1,numOfMatch_id.getInt("last_insert_rowid()"));
                        insert_question_query.setInt(2,i++);
                        insert_question_query.setFloat(3,question_grade);
                        insert_question_query.executeUpdate();
                    }
                    return numOfMatch_id.getInt("last_insert_rowid()");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }


    // ============= Submission Query ===============


    /**
     * Return a prepared SQL statement that, when executed, will
     * return one row for every question of the latest submission for the given exercise by the given user.
     * <p>
     * The rows should be sorted by QuestionId, and each row should contain:
     * - A column named "SubmissionId" with the submission id.
     * - A column named "QuestionId" with the question id,
     * - A column named "Grade" with the grade for that question.
     * - A column named "SubmissionTime" with the time of submission.
     * <p>
     * Parameter 1 of the prepared statement will be set to the User's username, Parameter 2 to the Exercise Id, and
     * Parameter 3 to the number of questions in the given exercise.
     * <p>
     * This will be used by {@link #getLastSubmission(User, Exercise)}
     *
     * @return
     */
    PreparedStatement getLastSubmissionGradesStatement() throws SQLException {
        PreparedStatement to_return = db.prepareStatement("SELECT Submission.SubmissionId, QuestionId, Grade ,SubmissionTime FROM Submission " +
                "INNER JOIN QuestionGrade  USING (SubmissionId) " +
                "INNER JOIN User  USING (UserId) "+
                "WHERE User.Username = ? AND Submission.ExerciseId = ? " +
                "ORDER BY  SubmissionTime DESC,QuestionId LIMIT ?");


        return to_return;
    }

    /**
     * Return a prepared SQL statement that, when executed, will
     * return one row for every question of the <i>best</i> submission for the given exercise by the given user.
     * The best submission is the one whose point total is maximal.
     * <p>
     * The rows should be sorted by QuestionId, and each row should contain:
     * - A column named "SubmissionId" with the submission id.
     * - A column named "QuestionId" with the question id,
     * - A column named "Grade" with the grade for that question.
     * - A column named "SubmissionTime" with the time of submission.
     * <p>
     * Parameter 1 of the prepared statement will be set to the User's username, Parameter 2 to the Exercise Id, and
     * Parameter 3 to the number of questions in the given exercise.
     * <p>
     * This will be used by {@link #getBestSubmission(User, Exercise)}
     *
     */
    PreparedStatement getBestSubmissionGradesStatement() throws SQLException {
         PreparedStatement to_return = db.prepareStatement("SELECT QuestionGrade.SubmissionId,QuestionGrade.QuestionId,QuestionGrade.Grade, SubmissionTime FROM QuestionGrade INNER JOIN " +
                 "(SELECT SubmissionId,QuestionGrade.QuestionId,Grade,SubmissionTime  FROM QuestionGrade  " +
                 "INNER JOIN Submission USING (SubmissionId) "+
                 "INNER JOIN Question ON QuestionGrade.QuestionId = Question.QuestionId AND Question.ExerciseId=Submission.ExerciseId " +
                 "INNER JOIN User USING (UserId)  " +
                 "WHERE User.Username = ?  AND Submission.ExerciseId = ? " +
                 "GROUP BY SubmissionId ORDER BY sum(Grade*Points) DESC ) AS t " +
                 "ON QuestionGrade.SubmissionId = t.SubmissionId " +
                 "LIMIT ? ");

        return to_return;
    }

    /**
     * Return a submission for the given exercise by the given user that satisfies
     * some condition (as defined by an SQL prepared statement).
     * <p>
     * The prepared statement should accept the user name as parameter 1, the exercise id as parameter 2 and a limit on the
     * number of rows returned as parameter 3, and return a row for each question corresponding to the submission, sorted by questionId.
     * <p>
     * Return null if the user has not submitted the exercise (or is not in the database).
     *
     * @param user
     * @param exercise
     * @return
     */
    Submission getSubmission(User user, Exercise exercise, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.username);
        stmt.setInt(2, exercise.id);
        stmt.setInt(3, exercise.questions.size());

        ResultSet res = stmt.executeQuery();

        boolean hasNext = res.next();
        if (!hasNext)
            return null;

        int sid = res.getInt("SubmissionId");
        Date submissionTime = new Date(res.getLong("SubmissionTime"));

        float[] grades = new float[exercise.questions.size()];

        for (int i = 0; hasNext; ++i, hasNext = res.next()) {
            grades[i] = res.getFloat("Grade");
        }

        return new Submission(sid, user, exercise, submissionTime, (float[]) grades);
    }

    /**
     * Return the latest submission for the given exercise by the given user.
     * <p>
     * Return null if the user has not submitted the exercise (or is not in the database).
     *
     * @param user
     * @param exercise
     * @return
     */
    public Submission getLastSubmission(User user, Exercise exercise) throws SQLException {
        return getSubmission(user, exercise, getLastSubmissionGradesStatement());
    }


    /**
     * Return the submission with the highest total grade
     *
     * @param user
     * @param exercise
     * @return
     */
    public Submission getBestSubmission(User user, Exercise exercise) throws SQLException {
        return getSubmission(user, exercise, getBestSubmissionGradesStatement());
    }
}
