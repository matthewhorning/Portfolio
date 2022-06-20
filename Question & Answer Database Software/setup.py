""" Create the schema for db based on db vendor in config.
In order to support another database, you can specify
a different dbtype in the config file.
"""
#pylint:disable=W1514
import sqlite3
from configparser import ConfigParser
from sqlite3 import Error
import json

def create_table(db_file, sql):
    """create table."""

    with sqlite3.connect(db_file) as conn:
        cur = conn.cursor()
        cur.execute(sql)

def execute_query(db_file, sql):
    """ execute a query. """

    try:
        with sqlite3.connect(db_file) as conn:
            cur = conn.cursor()
            cur.execute(sql)
            conn.commit()
    except Error as conn_error:
        #print(conn_error)
        raise

def setup():
    """call the setup"""

    config_object = ConfigParser()
    config_object.read("qa.conf")
    dbinfo = config_object['DB']

    if dbinfo['dbtype'] != 'sqlite':
        raise ValueError("Unsupported database.")

    with open("some_tables.json") as some_tab:
        tlist = json.loads(some_tab.read())
        for entry in tlist:
            create_table(dbinfo["dbname"], entry)
            print(f"Created table {entry} successfully!")

    with open("pop_data.json") as some_tab:
        tlist = json.loads(some_tab.read())
        for entry in tlist:
            try:
                execute_query(dbinfo["dbname"], entry)
            except:
                print("Data already exists, not populating.")

if __name__ == "__main__":
    setup()
