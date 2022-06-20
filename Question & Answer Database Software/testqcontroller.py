"""Tests for controller module."""
#pylint:disable=E0401
#pylint:disable=W0613
from configparser import ConfigParser
from model.question import Question
from controller.qcontroller import QController
from setup import setup

def create_controller():
    '''Creates a controller for the tests.'''
    setup()
    config_object = ConfigParser()
    config_object.read("qa.conf")
    dbinfo = config_object['DB']
    qcontroller = QController(dbinfo['dbtype'])
    return qcontroller

def clear_questions(controller):
    '''Utility function to remove all questions from DB.'''

    questions = controller.get_all_questions()
    for question in reversed(questions):
        question_id = question.get_question_id()
        print(question_id)
        controller.remove_question(question_id)
    questions = controller.get_all_questions()
    print("Printing DB")
    print(questions)

def get_question(controller, question_id):
    '''Gets a question based on ID'''
    questions = controller.get_questions()
    for question in questions:
        if question[0] == question_id:
            return question
    return None

def update_question(question_id):
    '''Removes a question by ID.'''
    print("Functionality not implemented!")

def test_add_question():
    '''Tests that adding a question returns it as the most recently added question.'''
    sample_q = Question("What does having a final class mean?",
                        1,
                        "final class",
                        "java",
                        "CSCI3300",
                        "Final classes cannot have subclasses.")
    test_controller = create_controller()
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)

    tags = {}
    not_not = 0
    tags['question_topic'] = (sample_q.get_question_topic_tag(), not_not)
    tags['language'] = (sample_q.get_language_tag(), not_not)
    tags['class_code'] = (sample_q.get_class_code_tag(), not_not)
    ops = 'AND'

    questions = test_controller.get_questions(tags, ops)
    check_q = questions[len(questions)-1]
    assert check_q.get_question_id() == sample_q.get_question_id()
    assert check_q.get_question() == sample_q.get_question()
    assert check_q.get_answer() == sample_q.get_answer()
    assert check_q.get_question_topic_tag() == sample_q.get_question_topic_tag()
    assert check_q.get_language_tag() == sample_q.get_language_tag()
    assert check_q.get_class_code_tag() == sample_q.get_class_code_tag()

    clear_questions(test_controller)

def test_remove_question():
    '''Test for verifying that questions can be removed from the database.'''

    test_controller = create_controller()
    clear_questions(test_controller)

    #Add a first question
    sample_q = Question("What does having a final class mean?",
                        1,
                        "final class",
                        "java",
                        "CSCI3300",
                        "Final classes cannot have subclasses.")
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)

    #Add a second question
    sample_q2 = Question("What does having a final method mean?",
                         1,
                         "final method",
                         "java",
                         "CSCI3300",
                         "Final method cannot overrided.")
    question_id2 = test_controller.insert_question(sample_q2)
    sample_q2.set_question_id(question_id2)
    questions = test_controller.get_all_questions()
    test_controller.remove_question(sample_q.get_question_id())

    questions = test_controller.get_all_questions()
    check_q = questions[0]
    assert check_q.get_question_id() == sample_q2.get_question_id()
    assert check_q.get_question() == sample_q2.get_question()

    clear_questions(test_controller)

def test_get_question():
    '''Test for verifying question getter.'''

    test_controller = create_controller()
    sample_q = Question("What does having a final method mean?",
                        1,
                        "final method",
                        "java",
                        "CSCI3300",
                        "Final method cannot overrided.")
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)

    question = test_controller.get_question(question_id)
    assert question.get_question_id() == question_id
    assert question.get_question() == sample_q.get_question()
    assert question.get_question_topic_tag() == sample_q.get_question_topic_tag()
    assert question.get_language_tag() == sample_q.get_language_tag()
    assert question.get_class_code_tag() == sample_q.get_class_code_tag()

    clear_questions(test_controller)

def test_update_question():
    '''Test for verifying that updating a question updates it.'''
    sample_q = Question("What does having a final method mean?",
                        1,
                        "final method",
                        "java",
                        "CSCI3300",
                        "Final method cannot overrided.")
    test_controller = create_controller()
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)
    #test_controller.update_question(sample_q)


test_add_question()
test_remove_question()
test_get_question()
#test_update_question()
