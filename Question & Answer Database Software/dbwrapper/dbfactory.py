""" A factory module to create readers and writers. """
#pylint: disable=E0401
from dbwrapper.sqlite_reader import SqliteReader
from dbwrapper.sqlite_writer import SqliteWriter

class DbFactory:
    """ Create db agnostic readers and writers. """

    reader = None
    writer = None

    def __init__(self, dbtype: str, dbname: str):
        """ Check dbtype at runtime to select db vendor. """

        self.dbname = dbname
        if dbtype == 'sqlite':
            self.reader = SqliteReader(self.dbname)
            self.writer = SqliteWriter(self.dbname)
        else:
            raise ValueError("Unsupported database")

    def get_reader(self):
        """ Return the reader created. """

        return self.reader

    def get_writer(self):
        """ Return the writer created. """

        return self.writer
