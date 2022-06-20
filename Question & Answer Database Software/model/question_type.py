"""This is the question type model."""

class QuestionType:
    """
    A class to represent a QuestionType.

    Attributes
    ----------
    questionTypeId : int
        The type id of the question.
    questionType: str
        The type of the question.

    """
    def __init__(self, question_type_id: int, question_type: str):
        """ Construct a QuestionType object. """

        self.question_type_id = question_type_id
        self.question_type = question_type

    def __str__(self):
        """ Print output as a string. """

        sobj = f"question_type_id: {self.question_type_id}\n"
        sobj += f"question_type: {self.question_type}"

        return sobj

    def get_question_type_id(self):
        """Get a question type id."""

        return self.question_type_id

    def get_question_type(self):
        """Get a question type."""

        return self.question_type
        