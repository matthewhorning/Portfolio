# Exam Generator Software

## This software assists professors in exam curation, generation, revision, and retrieval of previous exams.
Readme below contains plans for second and third sprints (part of assignment). Sprints were two weeks long with a week after each sprint dedicated to reflecting on the past sprint.

### Project Date: *Spring 2022*

## My Responsibilites

For this project, I wrote the tests for our project using the pytest library.

**Build Instructions**
```
python3 qdriver.py
```
**Test Instructions**

*From root directory, run:*
```
export PYTHONPATH=$(pwd)
```
*Navigate to desired testing directory and run:*
```
pytest
```
**Dependencies**

Requires Python3, pytest and [Python 3 tkinter](https://riptutorial.com/tkinter/example/3206/installation-or-setup)

# Sprint 2 Planning

## Goals

1. Create basic GUI interface using Python GUI library
    * Should be able to pull questions from database and display in list
    * Should be able to add, remove & update questions/answers via interface
2. Answers 
    * Add/remove/edit answers
    * Implement answer in the model
    * Adding answer table to pySQLite database schema
3. Questions
    * Implement remaining question types
4. Testing
    * PyTest
    * Test that our API is working as expected
    * Write tests before writing code
        * Adding, removing & editing questions
        * Same for answers
    * Manual testing for interface
5. Demonstration
    * Run tests to make sure the functions work
    * Show the adding of questions and answers then showing them in database
    * Show the removing of questions and answers from database
    * Show the ability to edit questions and answers

# Sprint 3 Planning

## Goals

1. Display all questions
    * Search by tag
2. Tags for questions
    * Type of question, language, class code (eg CSCI3300)
3. Questions
    * create date, update data, add tags
4. Improve user interface using 8 golden rules
    * Add search and filter features
5. Tests for all additional functionality
6. Export questions
    * Format? (txt file)

## Demo video

We will show that you can search by tag, the question information in our database, and show exporting questions