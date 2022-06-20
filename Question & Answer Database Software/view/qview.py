"""This is the View"""
#pylint: disable=E0401
import sys
from configparser import ConfigParser

from model.question import Question
from model.freeresponse import FreeResponse
from controller.qcontroller import QController
from setup import setup

class QView:
    """ View as in model view controller """

    def __init__(self):
        config_object = ConfigParser()
        config_object.read("qa.conf")
        dbinfo = config_object['DB']
        self.qcontroller = QController(dbinfo['dbtype'])

    def handle_add(self):
        """Handle an add record."""

        print("Enter question: ")
        qinput = input().strip()
        qt_input = 1
        while True:
            print("Select Number for Question Type: ")
            print("Free Response [1]")
            print("Multiple Choice [2]")
            print("Radio Choice [3]")
            print("Boolean [4]")
            try:
                qt_input = int(input().strip())
                if qt_input > 1:
                    print("Currently only free response is supported!\n")
                else:
                    break
            except ValueError:
                print("Please enter a valid number.\n")

        print("Enter free response answer: ")
        ainput = input().strip()
        question = Question(qinput, qt_input)
        question_id = self.qcontroller.insert_question(question)
        question.set_question_id(question_id)

        free_response = FreeResponse(question.get_question_id(), ainput)
        self.qcontroller.insert_freeresponse(free_response)
        print("Question and answer successfully added!\n")

    def handle_remove(self):
        """ Handle a remove record. """

        print("Enter questionId: ")
        qinput = input().strip()
        self.qcontroller.remove_question(int(qinput))
        print(f"Question {qinput} successfully removed!\n")

    def handle_init(self):
        """ This makes sure the db is ready. """

        try:
            self.qcontroller.get_question_types()
        except ValueError as print_error:
            print(print_error)
            setup()
            return

    def handle_view(self):
        """Handle a view."""

        self.handle_init()

        print("Welcome to the exam generator!\n")
        while True:
            print("Select a choice: AddQuestion(A), RemoveQuestion(R), ShowAll(S), Quit(Q)\n")
            choice = input().strip()
            if choice == "A":
                self.handle_add()
            elif choice == "R":
                self.handle_remove()
            elif choice == "S":
                print("Here are all the questions:")
                questions = self.qcontroller.get_questions()
                for row in questions:
                    print(row)
                print()
                print("Here are all the answers:")
                responses = self.qcontroller.get_freeresponses()
                for row in responses:
                    print(row)
            elif choice == "Q":
                sys.exit("Thank you your data has been saved!")
            else:
                print("Invalid Choice!\n")
                