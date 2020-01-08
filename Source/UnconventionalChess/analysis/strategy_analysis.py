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

    def get_chosen_move_score(self):
        return self.legal_move_score_map[self.source + self.target]

    def get_diff(self):
        max_score = max(list(self.legal_move_score_map.values()))
        chosen_move_score = self.get_chosen_move_score()
        return max_score - chosen_move_score

    def get_bin_index(self, num_bins):
        _, bins, _ = plt.hist(self.legal_move_score_map.values(), bins=num_bins)

        chosen_move_score = self.get_chosen_move_score()
        for idx, lower_limit in enumerate(bins):
            if idx == len(bins) - 1:
                return idx
            elif lower_limit <= chosen_move_score < bins[idx + 1]:
                return idx


def score_move_in_row(row):
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

    fig1 = plt.figure()
    ax1 = fig1.add_subplot(111)

    for idx, y in enumerate(ys):
        ax1.scatter(x, y, marker='o', label=f'Game {idx + 1}')

    plt.legend(loc='upper left')
    plt.xlabel("Turn")
    plt.ylabel("Difference between max and chosen move CP Score")
    plt.savefig("score_over_game.png")
    plt.show()

    # also plot with mating points filtered
    fig2 = plt.figure()
    ax2 = fig2.add_subplot(111)

    for idx, y in enumerate(ys):
        ax2.scatter(x, y, marker='o', label=f'Game {idx + 1}')

    plt.legend(loc='upper left')
    plt.xlabel("Turn")
    plt.ylabel("Difference between max and chosen move CP Score")
    plt.ylim((0, 4000))
    plt.savefig("score_over_game_filtered.png")
    plt.show()


def plot_move_choice_histogram(scores):
    num_bins = 5
    flattened_scores = [turn_score for game_scores in scores for turn_score in game_scores]
    bin_indices = [score.get_bin_index(num_bins) for score in flattened_scores]

    plt.cla()
    plt.hist(bin_indices, bins=np.arange(num_bins + 1) - 0.5, rwidth=0.9)
    plt.xlabel("Chosen Move Score Pentile")
    plt.ylabel("Frequency")
    plt.xticks([x for x in range(num_bins)])
    plt.savefig("frequency.png")
    plt.show()


def analyse_games(game_data):
    scores = [[score_move_in_row(row) for row in game] for game in game_data]
    plot_score_over_game(scores)
    plot_move_choice_histogram(scores)


number_of_experiments = 5
engine = chess.engine.SimpleEngine.popen_uci('stockfish')

games = read_games(number_of_experiments)
num_moves = reduce(lambda num, dataset: num + len(dataset), games, 0)
analyse_games(games)
