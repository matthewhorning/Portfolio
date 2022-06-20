'''Module for testing free response.'''
from freeresponse import FreeResponse

def test_set_answer():
    '''Test for verifying set answer functionality.'''
    test_controller = create_controller()
    sample_res = FreeResponse(0, "Answer")
    sample_res.set_answer("Answer2")
    assert sample_res.get_answer() == "Answer2";

def test_set_question_id():
    '''Test for verifying set question id functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_question_id(0)
    sample_a = FreeResponse(sample_q.get_question_id(), "Answer")
    assert sample_q.get_question_id() == sample_a.get_question_id()