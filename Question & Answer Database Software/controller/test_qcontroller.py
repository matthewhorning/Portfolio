"""Tests for controller module."""
import sys
from model.question import Question
from qcontroller import QController
from configparser import ConfigParser
from setup import setup

def create_controller():
    '''Creates a controller for the tests.'''
    setup()
    config_object = ConfigParser()
    config_object.read("qa.conf")
    dbinfo = config_object['DB']
    qcontroller = QController(dbinfo['dbtype'])
    return(qcontroller)

def clear_questions(controller):
    '''Utility function to remove all questions from DB.'''
    questions = controller.get_questions()
    for question in reversed(questions):
        question_id = question[0]
        print(question_id)
        controller.remove_question(question_id)
    questions = controller.get_questions()
    print("Printing DB")
    print(questions)

def get_question(controller, question_id):
    '''Gets a question based on ID'''
    questions = controller.get_questions()
    for question in questions:
        if (question[0] == question_id):
            return question
    return None

def update_question(question_id):
    '''Removes a question by ID.'''
    print("Functionality not implemented!")

def test_add_question():
    '''Tests that adding a question returns it as the most recently added question.'''
    sample_q = Question("What is my name?", 1, 'Loops', 'Python', 'CSCI1300', 'Matthew')
    test_controller = create_controller()
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)

    questions = test_controller.get_all_questions()
    check_q = questions[len(questions)-1]
    assert check_q.get_question_id() == sample_q.get_question_id()
    assert check_q.get_question() == sample_q.get_question()

def test_remove_question():
    '''Test for verifying that questions can be removed from the database.'''
    test_controller = create_controller()
    #clear_questions(test_controller)
    #Add a first question
    sample_q = Question("What is my name?", 1, 'Loops', 'Python', 'CSCI1300', 'Matthew')
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)
    #Add a second question
    sample_q2 = Question("What is my address?", 1, 'Loops', 'Python', 'CSCI1300', '1300 Yellow Street')
    question_id2 = test_controller.insert_question(sample_q2)
    sample_q2.set_question_id(question_id2)
    questions = test_controller.get_all_questions()
    test_controller.remove_question(sample_q2.get_question_id()) #

    questions = test_controller.get_all_questions()
    check_q = questions[len(questions)-1] #Get the last question in the frame
    assert check_q.get_question_id() == sample_q.get_question_id()
    assert check_q.get_question() == sample_q.get_question()

def test_get_question():
    '''Test for verifying get_question() functionality.'''
    test_controller = create_controller()
    sample_q = Question("What is my name?", 1, 'Loops', 'Python', 'CSCI1300', 'Matthew')
    sample_q2 = Question("What is my address?", 2, 'Loops', 'Python', 'CSCI1300', '1300 Yellow Street')
    question_id = test_controller.insert_question(sample_q)
    sample_q.set_question_id(question_id)
    question_id = test_controller.insert_question(sample_q2)
    sample_q2.set_question_id(question_id)
    assert test_controller.get_question(question_id).get_question() == sample_q2.get_question()