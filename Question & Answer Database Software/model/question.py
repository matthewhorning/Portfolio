"""This is the question model."""

class Question:
    """
    A class to represent a Question.

    Attributes
    ----------
    question : str
        The text of the question.
    question_type_id: int
        The type of the question.
    question_topic_tag: str
        The question topic tag for the question.
    language_tag: str
        The language tag for the question.
    class_code_tag: str
        The class code tag for the question.
    answer: str
        The answer which is only required for returning.

    """
    def __init__(self,
                 question,
                 question_type_id,
                 question_topic_tag,
                 language_tag,
                 class_code_tag,
                 answer):

        self.question_id = None
        self.question = question
        self.question_type_id = question_type_id
        self.question_topic_tag = question_topic_tag
        self.language_tag = language_tag
        self.class_code_tag = class_code_tag
        self.answer = answer
        self.create_date = None
        self.update_date = None

    def __str__(self):
        sobj = f"question_id: {self.question_id}\n"
        sobj += f"question: {self.question}\n"
        sobj += f"question_type_id: {self.question_type_id}\n"
        sobj += f"question_topic_tag: {self.question_topic_tag}\n"
        sobj += f"language_tag: {self.language_tag}\n"
        sobj += f"class_code_tag: {self.class_code_tag}\n"
        sobj += f"create_date: {self.create_date}\n"
        sobj += f"update_date: {self.update_date}\n"
        sobj += f"answer: {self.answer}\n"
        return sobj

    def get_question_id(self):
        """Get a question id."""
        return self.question_id

    def get_question(self):
        """Get a question."""
        return self.question

    def get_question_type_id(self):
        """Get a question type id"""
        return self.question_type_id

    def set_question_id(self, question_id):
        """Set a question id."""
        self.question_id = question_id

    def get_question_topic_tag(self):
        """Get a question_topic_tag"""
        return self.question_topic_tag

    def set_question_topic_tag(self, question_topic_tag):
        """Set a question_topic_tag."""
        self.question_topic_tag = question_topic_tag

    def get_language_tag(self):
        """Get a language tag"""
        return self.language_tag

    def set_language_tag(self, language_tag):
        """Set a language_tag."""
        self.language_tag = language_tag

    def get_class_code_tag(self):
        """Get a class code tag"""
        return self.class_code_tag

    def set_class_code_tag(self, class_code_tag):
        """Set a class_code_tag."""
        self.class_code_tag = class_code_tag

    def get_create_date(self):
        """Get a create date"""
        return self.create_date

    def set_create_date(self, create_date):
        """Set a create_date."""
        self.create_date = create_date

    def get_update_date(self):
        """Get a update date"""
        return self.update_date

    def set_update_date(self, update_date):
        """Set a update_date."""
        self.update_date = update_date

    def get_answer(self):
        """Get an answer"""
        return self.answer

    def set_answer(self, answer):
        """Set an answer."""
        self.answer = answer
