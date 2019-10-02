import React, {Component} from "react";
import {Provider} from "react-redux";
import {createStore} from "redux";
import GameContainer from "./components/GameContainer";

const initialState = {
    timeOrderedMessages: []
};

function reducer(state = initialState, action) {
    console.log('reducer', state, action);
    return state;
}

const store = createStore(reducer);

const App = () => (
    <Provider store={store}>
        <GameContainer/>
    </Provider>
);

export default App;

