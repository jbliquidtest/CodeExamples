from item import Item

from errors import ItemAlreadyExistsError, ItemNotExistError


class ShoppingCart:
    """
        This class is  for Shopping Cart
        methods:
            add_item(self, item) - Adds the given item to the shopping cart
            remove_item(self, item_name) - Removes the item with the given name from the shopping cart
            get_subtotal(self) – Returns the subtotal price of all the items currently in the shopping cart.
         Attributes:
            self.items (List): list of items in shopping cart.
            self.tags (List): list of hashtags that appears in items in the shopping cart.
        """

    def __init__(self):
        """
            The constructor for Shopping Cart class.
            Attributes:
            self.items (List): list of items in shopping cart.
            self.tags (List): list of hashtags that appears in items in the shopping cart.
                """

        self.items = list()
        self.tags = list()

    def add_item(self, item: Item):
        """
            The method Adds the given item to the shopping cart and add hashtags to tags list.
            :arg -
                self - the current instance of ShoppingCart
                item (Item) -instance of Item.
            :exception -  if the item already exists in the shopping cart, raises ItemAlreadyExistsError.
            """

        if item in self.items:
            raise ItemAlreadyExistsError("Item is already in your shopping cart ")
            return
        self.items.append(item)
        for tag in item.hashtags:
            self.tags.append(tag)

    def remove_item(self, item_name: str):
        """
            The method removes the item with the given name from the shopping cart and remove hashtags to tags list.
            :arg -
                self - the current instance of ShoppingCart
                item_name (str) -instance of str .
            :exception -  if no item with the given name exists, raises ItemNotExistError.
            """

        check = [item for item in self.items if item.name == item_name]
        if len(check) == 0:
            raise ItemNotExistError("Item is not exist in your shopping cart ")
            return
        self.items.remove(check[0])
        for tag in check[0].hashtags:
            self.tags.append(tag)

    def get_subtotal(self) -> int:
        """
            The method Returns the subtotal price of all the items currently in the shopping cart.
            :return
                int - sum of price of all items in the shopping cart
            """
        return sum(item.price for item in self.items)

    def is_in_cart(self,item_name: str):
        """
               The method check if item is in the shopping cart.
               :arg -
                   self - the current instance of ShoppingCart
                   item_name (str) -instance of item name to check .
               :return
                True - if item exist in shopping cart
                False - if not.
               """

        check = [item for item in self.items if item.name == item_name]
        if len(check) == 0:
            return False
        return True
