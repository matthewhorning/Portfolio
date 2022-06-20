"""This is the freeresponse model."""

class FreeResponse:
    """
    A class to represent a FreeResponse.

    Attributes
    ----------
    question_id : int
        The id of the question.
    answer: str
        The body of the answer.
    """
    def __init__(self, question_id: int, answer: str):
        """ Construct a free response object. """

        self.question_id = question_id
        self.answer = answer

    def __str__(self):
        """ Print output as a string. """

        sobj = f"question_id: {self.question_id}\n"
        sobj += f"answer: {self.answer}"

        return sobj

    def get_question_id(self):
        """Get a question id."""

        return self.question_id

    def set_question_id(self, question_id):
        """Set question id."""

        self.question_id = question_id

    def get_answer(self):
        """Get a answer."""

        return self.answer

    def set_answer(self, answer):
        """Set the answer."""

        self.answer = answer
