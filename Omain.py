from store import Store
from item import Item
from shopping_cart import ShoppingCart

POSSIBLE_ACTIONS = [
    'search_by_name',
    'search_by_hashtag',
    'add_item',
    'remove_item',
    'checkout',
    'exit'
]

ITEMS_FILE = 'items.yml'


def read_input():
    line = input('What would you like to do?')
    args = line.split(' ')
    return args[0], ' '.join(args[1:])


def main():
    store = Store(ITEMS_FILE)
    action, params = read_input()
    while action != 'exit':
        if action not in POSSIBLE_ACTIONS:
            print('No such action...')
            continue
        if action == 'checkout':
            print(f'The total of the purchase is {store.checkout()}.')
            print('Thank you for shopping with us!')
            return
        if action == 'exit':
            print('Goodbye!')
            return
        if action == 'search_by_name':
            print([item.name for item in store.search_by_name(params)])
        if action == 'search_by_hashtag':
            print([item.name for item in store.search_by_hashtag(params)])
        if action == 'add_item':
            store.add_item(params)
        if action == 'remove_item':
            store.remove_item(params)

        action, params = read_input()
        getattr(store, action)(params)
    

if __name__ == '__main__':
    main()
