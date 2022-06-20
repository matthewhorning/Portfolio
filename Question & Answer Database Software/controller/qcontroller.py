""" Controller is called by the view, handles a request. """
#pylint: disable=E0401
from configparser import ConfigParser

from dao.question_dao import QuestionDao
from dbwrapper.dbfactory import DbFactory
from model.question import Question
from model.question_type import QuestionType
from model.freeresponse import FreeResponse

class QController:
    """ Controller as in model view controller """

    def __init__(self, dbtype: str):
        """ Init the factory and readers """
        self.dao = QuestionDao()
        config_object = ConfigParser()
        config_object.read("qa.conf")
        dbinfo = config_object['DB']
        dbfactory = DbFactory(dbtype, dbinfo["dbname"])
        self.dao.set_reader(dbfactory.get_reader())
        self.dao.set_writer(dbfactory.get_writer())

    def get_question(self, question_id: int):
        """ Get a specific question. """

        row = self.dao.get_question(question_id)
        question = Question(row[1], row[2], row[3], row[4], row[5], "")
        question.set_question_id(row[0])
        question.set_create_date(row[6])
        question.set_update_date(row[7])

        return question

    def get_all_questions(self):
        """ Get all the questions. """

        rows = self.dao.get_all_questions()
        questions = []
        for row in rows:
            question = Question(row[1], row[2], row[3], row[4], row[5], row[6])
            question.set_question_id(row[0])
            question.set_create_date(row[7])
            question.set_update_date(row[8])
            questions.append(question)

        return questions

    def get_questions(self, tags, ops):
        """ Get all the questions that match the tags and op. """

        rows = self.dao.get_questions(tags, ops)
        questions = []
        for row in rows:
            question = Question(row[1], row[2], row[3], row[4], row[5], row[6])
            question.set_question_id(row[0])
            question.set_create_date(row[7])
            question.set_update_date(row[8])
            questions.append(question)

        return questions

    def get_freeresponses(self):
        """ Get all the freeresponses. """

        rows = self.dao.get_freeresponses()
        freeresponses = []
        for row in rows:
            free_response = FreeResponse(row[0], row[1])
            freeresponses.append(free_response)

        return freeresponses

    def get_question_types(self):
        """ Get all the question_types. """

        rows = self.dao.get_question_types()
        question_types = []
        for row in rows:
            question_type = QuestionType(row[0], row[1])
            question_types.append(question_type)

        return question_types

    def insert_question(self, question):
        """ Insert a question. """

        return self.dao.insert_question(question)

    def remove_question(self, question_id):
        """ Remove a specific question. """

        self.dao.remove_question(question_id)

    def insert_freeresponse(self, free_response):
        """ Insert a free_response. """

        return self.dao.insert_freeresponse(free_response)
