'''Module for testing question type.'''
from question_type import QuestionType

def test_question_type():
    '''Test creation of a free response questio type'''
    sample_qtype = QuestionType(1, "FREE_RESPONSE")

    assert sample_qtype.get_question_type_id() == 1
    assert isinstance(sample_qtype, QuestionType)
    assert sample_qtype.get_question_type() == "FREE_RESPONSE"