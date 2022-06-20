""" Sqlite specific reader. """
import sqlite3

class SqliteReader():
    """ Module for handling reading from Sqlite. """

    def __init__(self, db_name):
        """ Construct handle based on db name. """

        self.db_name = db_name

    def query(self, sql):
        """ Generic query execution and return list. """

        with sqlite3.connect(self.db_name) as conn:
            cur = conn.cursor()
            try:
                print(f"sql = {sql}")
                cur.execute(sql)
            except sqlite3.OperationalError as oper_error:
                raise ValueError(oper_error)
            return cur.fetchall()

    def execute_simple(self, table, **kwargs):
        """ Dynamic predicate construction and execution. """

        sub_predicates = []
        for args in kwargs:
            val = kwargs[args]
            print(f"val = {val}")
            if val is not None and val != '':
                sub_predicates.append(f"{args}='{val}'")

        if len(sub_predicates) > 0:
            predicate = ' AND '.join(sub_predicates)
            query = f'SELECT * FROM {table} WHERE {predicate}'
        else:
            query = f'SELECT * FROM {table}'

        return self.query(query)

    def execute(self, table_list, col_list, ops, join_key, **kwargs):
        """ Dynamic predicate construction and execution. """

        print(f"ops = {ops}")
        sub_predicates = []
        for args in kwargs:
            val = kwargs[args][0]
            print(f"val = {val}")
            if val is not None and val != '':
                not_op = "!=" if kwargs[args][1] == 1 else "="
                sub_predicates.append(f"{args}{not_op}'{val}'")

        tables = table_list.split(',')

        if len(tables) > 2:
            raise ValueError("Too many tables to join!")

        if len(tables) > 1:
            if join_key is None or len(join_key) == 0:
                raise ValueError("Invalid join_key specified!")
            sub_predicates.append(f"{tables[0]}.{join_key} = {tables[1]}.{join_key}")

        if len(sub_predicates) > 0:
            predicate = f' {ops} '.join(sub_predicates)
            query = f'SELECT {col_list} FROM {table_list} WHERE {predicate}'
        else:
            query = f'SELECT {col_list} FROM {table_list}'

        return self.query(query)
