from functools import reduce
from typing import List

import matplotlib.pyplot as plt
import numpy as np
import chess
import chess.engine
import csv
import sys

count = 0


def loading_bar(count, total, size):
    percent = float(count) / float(total) * 100
    sys.stdout.write("\r" + str(int(count)).rjust(3, '0') + "/" + str(int(total)).rjust(3, '0') + ' [' + '=' * int(
        percent / 10) * size + ' ' * (10 - int(percent / 10)) * size + ']')


def parse_experiment(game_index):
    with open(f'experiment{game_index}.csv', newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter=',')
        next(reader)
        return [row for row in reader]


def read_games(num_games) -> List[List]:
    game_list = []
    for game_index in range(1, num_games + 1):
        game_list.append(parse_experiment(game_index))

    return game_list


class TurnScore:
    def __init__(self, source, target, fen, legal_move_score_map):
        self.source = source.lower()
        self.target = target.lower()
        self.fen = fen
        self.legal_move_score_map = legal_move_score_map

    def get_diff(self):
        max_score = max(list(self.legal_move_score_map.values()))
        chosen_move_score = self.legal_move_score_map[self.source + self.target]
        return max_score - chosen_move_score


def score(row):
    global count
    source = row[0]
    target = row[1]
    fen = row[2]

    board = chess.Board(fen)
    legal_move_score_map = {}
    for el in board.legal_moves:
        result = engine.play(board, root_moves=[el], limit=chess.engine.Limit(time=0.01), info=chess.engine.INFO_SCORE)
        legal_move_score_map[el.uci()] = abs(result.info.score.relative.score(mate_score=10000))

    count = count + 1
    loading_bar(count, num_moves, 4)
    return TurnScore(source, target, fen, legal_move_score_map)


def match_length(game, max_length):
    if len(game) < max_length:
        difference = max_length - len(game)
        game.extend([None] * difference)

    return game


def plot_score_over_game(game_scores):
    max_length = reduce(lambda a, b: max(a, len(b)), game_scores, 0)
    x = np.linspace(0, max_length, max_length)
    ys = [[turn_score.get_diff() for turn_score in game] for game in game_scores]
    ys = [match_length(game, max_length) for game in ys]

    fig = plt.figure()
    ax1 = fig.add_subplot(111)

    for idx, y in enumerate(ys):
        ax1.scatter(x, y, marker='o', label=f'Game {idx + 1}')

    plt.legend(loc='upper left')
    plt.title("Difference between best move at each turn for multiple games")
    plt.savefig("score_over_game.png")
    plt.show()


def plot_move_choice_histogram(scores):
    pass


def analyse_games(game_data):
    scores = [[score(row) for row in game] for game in game_data]
    plot_score_over_game(scores)
    plot_move_choice_histogram(scores)


number_of_experiments = 5
engine = chess.engine.SimpleEngine.popen_uci('stockfish')

games = read_games(number_of_experiments)
num_moves = reduce(lambda num, dataset: num + len(dataset), games, 0)
analyse_games(games)
