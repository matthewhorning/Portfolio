''' File containing graphical user interface. '''
# disabling:
# pylint: disable=E0401
# pylint: disable=W0105
# pylint: disable=W0401
# pylint: disable=R0915
# pylint: disable=C0103
# pylint: disable=W0614
import tkinter.messagebox
import os
from tkinter import *
from model.question import Question
from controller.qcontroller import QController
from setup import setup
#root = tk.Tk()
#root.geometry("200x100")

#textExample = tk.Entry(root)

''' Beginning state of user interface. '''
def start():
    """start method"""

    ''' Create data table for questions and answers to be stored. '''
    setup()
    qcontroller = QController("sqlite")

    ''' Create window for gui. '''
    ui = Tk()
    #questionEntry = ui.Entry(root)


    ui.geometry('1200x400')

    ''' Functions of add, edit, and remove buttons. '''
    def add():
        if len(question_entry.get()) == 0:
            tkinter.messagebox.showinfo('Enter a question', 'Please enter a question')
        elif len(answer_entry.get()) == 0:
            tkinter.messagebox.showinfo('Enter an answer', 'Please enter an answer')
        else:
            question = Question(question_entry.get(),
                                1,
                                question_topic_tag_entry.get(),
                                language_tag_entry.get(),
                                class_code_tag_entry.get(),
                                answer_entry.get()) # the answer

            # insert question and the answer
            question_id = qcontroller.insert_question(question)
            question.set_question_id(question_id)

            tkinter.messagebox.showinfo('add question', 'Question and answer have been stored!')

    def edit():
        if len(question_id_entry.get()) == 0:
            tkinter.messagebox.showinfo('Enter ID message', 'Please enter a question ID')
        else:
            tkinter.messagebox.showinfo('edit message', 'Your question has been changed!')

    def delete():
        if len(question_id_entry.get()) == 0:
            tkinter.messagebox.showinfo('Enter ID message', 'Please enter a question ID')
        else:
            qcontroller.remove_question(question_id_entry.get())
            tkinter.messagebox.showinfo('delete message', 'Your question has been deleted!')

    def get_questions():
        #tkinter.messagebox.showinfo('Show questions', qcontroller.get_questions())
        tags = {}
        tags['question_topic'] = (question_topic_tag_entry.get(), question_topic_not.get())
        tags['language'] = (language_tag_entry.get(), language_not.get())
        tags['class_code'] = (class_code_tag_entry.get(), class_code_not.get())
        op = operator_var.get()
        print(f"op = {op}")
        qlist = qcontroller.get_questions(tags, op)
        questions = '\n'.join([str(q) for q in qlist])
        return questions

    def view():
        questions = get_questions()
        tkinter.messagebox.showinfo('Show questions', questions)

    def export():
        questions = get_questions()
        qfile = f"export{os.getpid()}.txt"
        with open(qfile, "w") as qo:
            qo.write(questions)
        print(f"Exported questions to file: {qfile} successfully!")


    ''' Text bar to enter questions and answers. '''
    Label(ui, text='Question:').grid(row=0)
    question_entry = Entry(ui, width=50)
    question_entry.grid(row=0, column=2)

    ''' Text bar to enter answers. '''
    Label(ui, text='Answer:').grid(row=1)
    answer_entry = Entry(ui, width=50)
    answer_entry.grid(row=1, column=2)

    ''' Text bar to search for question ids. '''
    Label(ui, text='Question ID:').grid(row=2)
    question_id_entry = Entry(ui, width=50)
    question_id_entry.grid(row=2, column=2)

    ''' Text bar for questionTopicTag '''
    Label(ui, text='Question Topic Tag:').grid(row=3)
    question_topic_tag_entry = Entry(ui, width=50)
    question_topic_tag_entry.grid(row=3, column=2)
    question_topic_not = tkinter.IntVar()
    question_topic_checkbox = Checkbutton(ui,
                                          text="NOT",
                                          variable=question_topic_not,
                                          onvalue=1,
                                          offvalue=0)
    question_topic_checkbox.grid(row=3, column=3)

    ''' Text bar for languageTag '''
    Label(ui, text='Language Tag:').grid(row=4)
    language_tag_entry = Entry(ui, width=50)
    language_tag_entry.grid(row=4, column=2)
    language_not = tkinter.IntVar()
    language_tag_checkbox = Checkbutton(ui,
                                        text="NOT",
                                        variable=language_not,
                                        onvalue=1,
                                        offvalue=0)
    language_tag_checkbox.grid(row=4, column=3)

    ''' Text bar for classCodeTag '''
    Label(ui, text='Class Code Tag:').grid(row=5)
    class_code_tag_entry = Entry(ui, width=50)
    class_code_tag_entry.grid(row=5, column=2)
    class_code_not = tkinter.IntVar()
    class_code_tag_checkbox = Checkbutton(ui,
                                          text="NOT",
                                          variable=class_code_not,
                                          onvalue=1,
                                          offvalue=0)
    class_code_tag_checkbox.grid(row=5, column=3)

    ''' Radio bar for operator '''
    Label(ui, text='Operator:').grid(row=6)
    operator_var = tkinter.StringVar()
    operator_var.set("AND")
    op_radio_1 = Radiobutton(ui, text="AND", variable=operator_var, value="AND")
    op_radio_1.grid(row=6, column=2)
    op_radio_2 = Radiobutton(ui, text="OR", variable=operator_var, value="OR")
    op_radio_2.grid(row=6, column=3)


    ''' Add question, edit question, view questions and delete question buttons. '''
    add_button = Button(ui, text="Add Question", width=20, height=5, command=add)
    add_button.place(x=100, y=250)

    edit_button = Button(ui, text="Edit Question", width=20, height=5, command=edit)
    edit_button.place(x=300, y=250)

    delete_button = Button(ui, text="Delete Question", width=20, height=5, command=delete)
    delete_button.place(x=500, y=250)

    view_button = Button(ui, text="View/Search Questions", width=20, height=5, command=view)
    view_button.place(x=700, y=250)

    export_button = Button(ui, text="Export Questions", width=20, height=5, command=export)
    export_button.place(x=900, y=250)

    ''' Loop that runs gui until closed out. '''
    ui.mainloop()
