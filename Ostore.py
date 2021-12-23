import yaml

from item import Item
from shopping_cart import ShoppingCart
from errors import ItemAlreadyExistsError, ItemNotExistError, TooManyMatchesError


class Store:
    """
        This class is  for Store
        methods:
            __init__(self, path) - The constructor for Store class
            get_items(self) - return a list of items that in the shop
            search_by_name(self, item_name: str) - return a sorted list of all the items that match the search term
            check_tags(self, item) - return the number of time that items hashtags appears in tags list of shopping cart
            search_by_hashtag(self, hashtag: str) - return sorted list of all the items matching the phrase.
            add_item(self, item) - Adds an item with the given name to the customer’s shopping cart
            remove_item(self, item_name) - Removes an item with the given name from the customer’s shopping cart
            checkout(self) - Returns the total price of all the items in the costumer’s shopping cart.

         Attributes:
            self._items (List): list of items in Store.
            self._shopping_cart (ShoppingCart): costumer’s shopping cart object.
        """

    def __init__(self, path):
        """
            The constructor for Shopping Cart class.
            Attributes:
            self._items (List): list of items in Store.
            self._shopping_cart (ShoppingCart): costumer’s shopping cart object.
        """

        with open(path) as inventory:
            items_raw = yaml.load(inventory, Loader=yaml.FullLoader)['items']
        self._items = self._convert_to_item_objects(items_raw)
        self._shopping_cart = ShoppingCart()

    @staticmethod
    def _convert_to_item_objects(items_raw):
        return [Item(item['name'],
                     int(item['price']),
                     item['hashtags'],
                     item['description'])
                for item in items_raw]

    def get_items(self) -> list:
        """
            The method returns a list of all items in the Store .
            :return
                self._items - list of all items in the Store
            """

        return self._items

    def search_by_name(self, item_name: str) -> list:
        """
               The method return a sorted list of all the items that match the search term.
               :arg -
                   self - the current instance of Store
                   item_name (str) -instance of item name to check .
               :return
                    check (List) - a sorted list of all the items that match the search term.
               """

        check = [item for item in self._items if
                 item_name in item.name and not self._shopping_cart.is_in_cart(item.name)]
        check.sort(key=lambda item: (self.check_tags(item), item.name))
        return check

    def check_tags(self, item):
        """
               The method check the number of time that items hashtags appears in tags list of shopping cart.
               :arg -
                   self - the current instance of Store
                   item_name (str) -instance of item to check .
               :return
                    count (int) - number of time that items hashtags appears in tags list of shopping cart.
               """

        count = 0
        for has in item.hashtags:
            if has in self._shopping_cart.tags:
                count = count - self._shopping_cart.tags.count(has)
        return count

    def search_by_hashtag(self, hashtag: str) -> list:
        """
               The method return sorted list of all the items matching the phrase.
               :arg -
                   self - the current instance of Store
                   hashtag (str) -instance of hashtag  to check .
               :return
                    check (List) - a sorted list of all the items matching the phrase.
               """

        check = [item for item in self._items if
                 hashtag in item.hashtags and not self._shopping_cart.is_in_cart(item.name)]
        check.sort(key=lambda item: (self.check_tags(item), item.name))
        return check

    def add_item(self, item_name: str):
        """
            The method Adds an item with the given name to the customer’s shopping cart.
            :arg -
                self - the current instance of Store
                item (Item) -instance of str Item name to add.
            :exception -
                if no such item exists, raises ItemNotExistError.
                if there are multiple items matching the given name, raises TooManyMatchesError.
                If the given item is already in the shopping cart, raises ItemAlreadyExistsError
            """

        check = [item for item in self._items if item_name in item.name]
        if len(check) == 0:
            raise ItemNotExistError("Item is not exist in the Store")
        if len(check) >= 2:
            raise TooManyMatchesError("Too many items matching you string, plz by more accurate")
        try:
            self._shopping_cart.add_item(check[0])
        except ItemAlreadyExistsError as e:
            raise ItemAlreadyExistsError(e)

    def remove_item(self, item_name: str):
        """
            The method Removes an item with the given name from the customer’s shopping cart.
            :arg -
                self - the current instance of Store
                item (Item) -instance of str Item name to remove.
            :exception -
                if no such item exists in the Store or in customer’s shopping cart , raises ItemNotExistError.
                if there are multiple items matching the given name, raises TooManyMatchesError.
            """

        check = [item for item in self._shopping_cart.items if item_name in item.name]
        if len(check) == 0:
            raise ItemNotExistError("Item is not exist in the Store")
        if len(check) >= 2:
            raise TooManyMatchesError("Too many items matching you string, plz by more accurate")
        try:
            self._shopping_cart.remove_item(check[0].name)
        except ItemNotExistError as e:
            raise ItemNotExistError(e)

    def checkout(self) -> int:
        """
            The method  Returns the total price of all the items in the costumer’s shopping cart.
            :return
                int - total price of all the items
            """

        return self._shopping_cart.get_subtotal()

