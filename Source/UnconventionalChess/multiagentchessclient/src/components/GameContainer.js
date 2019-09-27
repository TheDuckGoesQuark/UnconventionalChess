import React, {Component} from "react";

class GameContainer extends Component {

    constructor(props) {
        super(props);

        state = {
            // initial configuration for game (player, agent configs)
            initialConfig: undefined
        }
    };


    render() {
        const {initialConfig} = this.state;

        const configView = <ConfigView onConfirmation={this.onConfirmation} />;
        const gameView = <GameView onExit={this.onGameExit} />;

        return (
            {view}
        );
    }

}