from typing import List

import chess
import chess.engine
import csv

number_of_experiments = 5
engine = chess.engine.SimpleEngine.popen_uci('stockfish')


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
        self.source = source
        self.target = target
        self.fen = fen
        self.legal_move_score_map = legal_move_score_map


def score(row):
    source = row[0]
    target = row[1]
    fen = row[2]

    board = chess.Board(fen)
    legal_move_score_map = {}
    for el in board.legal_moves:
        result = engine.play(board, root_moves=[el], limit=chess.engine.Limit(time=0.1), info=chess.engine.INFO_SCORE)
        legal_move_score_map[el] = abs(result.info.score.relative)
        return TurnScore(source, target, fen, legal_move_score_map)


def analyse_games(game_data):
    scores = [[score(row) for row in game] for game in game_data]
    # evaluate average score, and score over the course of a game (i.e. do they get worse or better)


games = read_games(number_of_experiments)

analyse_games(games)
