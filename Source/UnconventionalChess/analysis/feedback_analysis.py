import csv

import matplotlib.pyplot as plt
import numpy as np


def parse_feedback():
    with open('feedback.csv', newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter=',')
        questions = next(reader)
        return [row for row in reader], questions


def create_filename(question_idx):
    return f'{question_idx}.png'


def plot_boolean(answers, question, question_idx):
    fig = plt.figure()
    ax = fig.add_subplot(111)
    num_bins = 2
    ax.hist(answers, bins=np.arange(num_bins + 1) - 0.5, rwidth=0.9)
    y_range = range(0, 12)
    ax.set_yticks(y_range)
    ax.set_ylabel("Frequency")
    ax.set_title(question)
    plt.savefig(create_filename(question_idx), transparent=True)
    plt.show()


def plot_likert(answers, question, question_idx):
    fig = plt.figure()
    ax = fig.add_subplot(111)
    num_bins = 5
    counts, _, _ = ax.hist(answers, bins=np.arange(num_bins + 1) + 0.5, rwidth=0.9)

    y_range = range(0, 12)
    ax.set_yticks(y_range)

    x_ticks_labels = ['Strongly Disagree', 'Disagree', 'Neutral', 'Agree', 'Strongly Agree']
    ax.set_xticks(range(1, 6))
    ax.set_xticklabels(x_ticks_labels, rotation=45)
    ax.set_ylabel("Frequency")
    ax.set_title(question)

    plt.savefig(create_filename(question_idx), transparent=True)
    plt.show()


def hist_per_question(feedback_rows, question_statements):
    num_questions = 18
    question_answers = [[] for _ in range(num_questions)]

    # get answer for each question
    for row in feedback_rows:
        for question_idx in range(num_questions):
            if question_idx == 0:
                value = row[question_idx]
            else:
                value = int(row[question_idx])

            question_answers[question_idx].append(value)

    for answers in question_answers:
        answers.sort()

    for idx, answers in enumerate(question_answers):
        if idx == 0:
            plot_boolean(answers, question_statements[idx], idx)
        else:
            plot_likert(answers, question_statements[idx], idx)


feedback, questions = parse_feedback()
hist_per_question(feedback, questions)
