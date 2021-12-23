def remove_forbidden_characters(sentence, forbidden_characters):
    new_str = sentence
    for c in forbidden_characters:
        new_str = new_str.replace(c, "")
    # replace `pass` with your code
    print(new_str)


def reverse(word):
    return word[::-1]


def is_palindrome(word):
    for i in range(0, len(word) - 1):
        if word[i] != word[len(word) - 1 - i]:
            return False
    return True


def find_largest_palindrome():
    num1 = 0
    for i in range(100, 999):
        for j in range(100, 999):
            mult = i * j
            if is_palindrome(str(mult)) and mult > num1:
                num1 = mult
    num2 = str(num1)[0:len(str(num1)):2]
    print(str(num1) + "," + str(num2))


def validate_israeli_id(id_number):
    mult = ""
    sum = 0
    i = 1
    for c in id_number:
        temp = str(int(c) * i)
        print(temp)
        if len(temp) > 1:
            sum = 0
            for j in range(0, len(temp)):
                sum += int(temp[j])
            mult += str(sum)
        else:
            mult += temp
        if i == 1:
            i = 2
        else:
            i = 1
    sum = 0
    for i in range(0, len(mult)):
        sum += int(mult[i])
    if sum % 10 == 0:
        print("valid")
    else:
        print("invalid")


# def evaluate_formula(formula):
#    if(len(formula == 0)):
#    print(formula[0])
def evaluate_formula(formula):
    """
    Evaluate a given expression in prefix notation.
    Asserts that the given expression is valid.
    """
    stack = []
    # is_whitespace = False
    temp = ""
    # print(type(temp))
    for c in formula[::-1]:
        # print("this is c",c)
        if c.isdigit() or c == '.':
            temp = c + temp
            # print("this is temp",temp)
        if c.isspace() and temp != "":
            # is_whitespace = True
            # print(temp)
            stack.append(float(temp))
            temp = ""
        if not c.isdigit() and not c.isspace():
            if len(stack) >= 2:
                o1 = stack.pop()
                o2 = stack.pop()
                if c == '+':
                    # print(o1 + o2)
                    stack.append(o1 + o2)
                elif c == '-':
                    # print(o1 - o2)
                    stack.append(o1 - o2)
                elif c == '*':
                    # print(o1 * o2)
                    stack.append(o1 * o2)
                elif c == '/':
                    # print(o1 ,o2)
                    # print(o1/o2)
                    stack.append(o1 / o2)
    if len(stack):
        res = stack.pop()
    print(res)


# def main():
#   """Read from stdin, solve the problem, write answer to stdout."""
#   input = sys.stdin.readline().split()
#   A = [int(x) for x in input[0].split(",")]
#   sys.stdout.write(str(solution(A)))



def solution(A):
    global rows
    B = 0
    row = 0
    isTrue = False
    for a in A:
        if a > B:
            B = a
            row = row +1

    sol = row
    return int(sol)


def a_solution(S: str) -> str:
    # sys.stderr.write(
    #   'Tip: Use sys.stderr.write() to write debug messages on the output tab.\n'
    # )
    res = ""
    a = list(S)
    print(len(S))
    for i in range(0, len(S)):
        # print("AAA")
        if a[i] == "?":
            if i == 0:
                if a[i + 1] == "1":
                    temp = "2"
                elif a[i + 1] == "2":
                    temp = "1"
                else:
                    temp = "1"
                a[i] = temp

            else:
                if a[i - 1] == "1":
                    print(i)
                    if i != len(S) and a[i + 1] != "2":
                        temp = "2"
                    else:
                        temp = "3"
                elif a[i - 1] == "2":
                    if i != len(S) and a[i + 1] != "1":
                        temp = "1"
                    else:
                        temp = "3"
                else:
                    temp = "1"
                a[i] = temp

    return "".join(a)


if __name__ == "__main__":
    # remove_forbidden_characters('Awesome sentence!', 'aeos')
    # print(reverse("abcd"))
    # print(is_palindrome("ABCA"))
    # find_largest_palindrome()
    # validate_israeli_id("123456782")
    # test_expression = "* -1.2 -1.2"
    # evaluate_formula(test_expression)
    # print(solution([5, 4, 3, 6, 1]))
    print(a_solution("?1???2???3???1?"))
