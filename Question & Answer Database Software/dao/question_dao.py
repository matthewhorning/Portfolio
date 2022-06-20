""" Data Access Object for Question. """

from model.freeresponse import FreeResponse

class QuestionDao:
    """ This is a DAO for question. """

    def __init__(self):
        """ Create a dao. """

        self.reader = None
        self.writer = None

    def set_reader(self, reader):
        """ Must initialize this reader. """

        self.reader = reader

    def set_writer(self, writer):
        """ Must initialize this writer. """

        self.writer = writer

    def get_question(self, question_id: int) -> list:
        """ Execute query based on a question id. """

        if self.reader is None:
            raise ValueError("reader is not initialized")

        qlist = self.reader.execute_simple("question", questionId=question_id)

        return qlist[0]

    def get_all_questions(self) -> list:
        """ Execute a query to get all questions. """

        if self.reader is None:
            raise ValueError("reader is not initialized")

        tags = {}
        tags['question_topic'] = (None, 0)
        tags['language'] = (None, 0)
        tags['class_code'] = (None, 0)
        ops = 'AND'
        return self.reader.execute("question, answer_free",
                                   "question.questionId, question,\
                                   questionTypeId, questionTopicTag,\
                                   languageTag, classCodeTag, answer,\
                                   createDate, updateDate",
                                   ops,
                                   "questionId",\
                                   questionTopicTag=tags['question_topic'],
                                   languageTag=tags['language'],
                                   classCodeTag=tags['class_code'])

    def get_questions(self, tags, ops) -> list:
        """ Execute a search query to get questions by tags. """

        if self.reader is None:
            raise ValueError("reader is not initialized")

        return self.reader.execute("question, answer_free",
                                   "question.questionId,\
                                   question, questionTypeId,\
                                   questionTopicTag, languageTag,\
                                   classCodeTag, answer,\
                                   createDate, updateDate",
                                   ops,
                                   "questionId",
                                   questionTopicTag=tags['question_topic'],
                                   languageTag=tags['language'],
                                   classCodeTag=tags['class_code'])

    def get_freeresponses(self) -> list:
        """ Execute a query to get all freeresponses. """

        if self.reader is None:
            raise ValueError("reader is not initialized")

        return self.reader.execute("answer_free")

    def get_question_types(self) -> list:
        """ Execute a query to get all question_types. """

        if self.reader is None:
            raise ValueError("reader is not initialized")

        return self.reader.execute("question_type")

    def insert_question(self, question) -> int:
        """ Execute an insert query and return last inserted id. """

        if self.writer is None:
            raise ValueError("writer is not initialized")

        # These need to be part of a transaction
        inserted_id = self.writer.insert("question",
                                         question=question.get_question(),
                                         questionTypeId=question.get_question_type_id(),
                                         questionTopicTag=question.get_question_topic_tag(),
                                         languageTag=question.get_language_tag(),
                                         classCodeTag=question.get_class_code_tag())

        answer = FreeResponse(inserted_id, question.get_answer())
        self.insert_freeresponse(answer)

        return inserted_id

    def remove_question(self, question_id: int):
        """ Remove the question and the corresponding answer. """

        if self.writer is None:
            raise ValueError("writer is not initialized")

        self.writer.delete("question", questionId=question_id)
        self.writer.delete("answer_free", questionId=question_id)

    def insert_freeresponse(self, free_response):
        """ Execute an insert query. """

        if self.writer is None:
            raise ValueError("writer is not initialized")

        self.writer.insert("answer_free",
                           questionId=free_response.get_question_id(),
                           answer=free_response.get_answer())
