import React from "react";
import {Provider} from "react-redux";
import {createStore, compose, applyMiddleware} from "redux";
import GameContainer from "./components/view/ViewRouter";
import rootReducer from "./components/RootReducer";
import gameService from "./middleware/gameService";

const composedEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
    rootReducer,
    composedEnhancer(applyMiddleware(gameService))
);

const App = () => (
    <Provider store={store}>
        <GameContainer/>
    </Provider>
);

export default App;

