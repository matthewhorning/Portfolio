""" Sqlite specific module for writing. """
import sqlite3

class SqliteWriter():
    """ Writer for inserts and deletes. """

    def __init__(self, db_name):
        """ Construct a writer based on db name. """

        self.db_name = db_name

    def insert(self, table, **kwargs):
        """ Insert a single row based on arguments. """

        cols = []
        vals = []
        qmarks = []
        for args in kwargs:
            cols.append(args)
            qmarks.append('?')
            vals.append(kwargs[args])

        vals = tuple(vals)
        cols = ','.join(cols)
        qm_str = ','.join(qmarks)

        query = f'INSERT INTO {table} ({cols}) VALUES({qm_str})'

        print(f"insert query = {query} {vals}")

        inserted_id = None

        with sqlite3.connect(self.db_name) as conn:
            cur = conn.cursor()
            cur.execute(query, vals)
            inserted_id = cur.lastrowid
            conn.commit()

        return inserted_id

    def delete(self, table, **kwargs):
        """ Delete rows based on arguments. """

        vals = []
        qmarks = []
        for args in kwargs:
            # this syntax works only for predicates with int type
            # for string you will need to use a single quote around the
            # question mark "?" for instance
            # DELETE FROM sometable WHERE name='Bob'
            # Note the single quote around Bob, since name is a VARCHAR
            # and not an INTEGER type
            qmarks.append(f'{args}=?')
            vals.append(f"{kwargs[args]}")

        qm_str = ' AND '.join(qmarks)
        vals = tuple(vals)

        query = f'DELETE FROM {table} WHERE {qm_str}'

        with sqlite3.connect(self.db_name) as conn:
            cur = conn.cursor()
            cur.execute(query, vals)
            conn.commit()
