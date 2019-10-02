import React, {Component} from "react";

const playAsWhiteButton = () => {
    return <button onClick={() => this.startGame(true)}>White</button>;
};

const playAsBlackButton = () => {
    return <button onClick={() => this.startGame(false)}>Black</button>;
};

const ConfigView = () => (
    <div>
        <h1>Hey you're looking at the config view, good on you buddy!</h1>
        <h3>Who do you want to play as:</h3>
        {playAsWhiteButton()}
        {playAsBlackButton()}
    </div>
);

export default ConfigView;

