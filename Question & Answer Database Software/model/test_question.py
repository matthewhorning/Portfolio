'''Module for testing question.py.'''
from model.question import Question

def test_generate_question():
    '''Tests generating a question'''
    sample_q = Question("What is my name?", 1)

    assert isinstance(sample_q, Question)

def test_set_question_id():
    '''Tests setting a question ID'''
    sample_q = Question("What is my name?", 1)
    sample_q.set_question_id(24)
    assert sample_q.get_question_id() == 24
    sample_q.set_question_id(sample_q.get_question_id() + 24)
    assert sample_q.get_question_id() == 48

def test_question_type():
    '''Tests initialization of question properly sets Question Type'''
    sample_q = Question("What is my name?", 1)
    assert sample_q.get_question_type_id() == 1

def test_question_topic_tag():
    '''Test for verifying topic tag functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_question_topic_tag("Programming Languages")
    assert sample_q.get_question_topic_tag() == "Programming Languages"

def test_question_language_tag():
    '''Test for verifying language tag functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_language_tag("Lua")
    assert sample_q.get_language_tag() == "Lua"

def test_class_code_tag():
    '''Test for verifying class code tag functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_class_code_tag(2100)
    assert sample_q.get_class_code_tag() == 2100

def test_update_date():
    '''Test for verifying update date functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_update_date("5/8/2022")
    assert sample_q.get_update_date() == "5/8/2022"

def test_set_answer():
    '''Test for verifying update answer functionality.'''
    sample_q = Question("What is my name?", 1, "Algorithms", "Python", 1300)
    sample_q.set_question_id(0)
    sample_a = FreeResponse(sample_q.get_question_id(), "Answer")
    assert sample_q.get_answer() == sample_a