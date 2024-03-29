import React from "react";
import {Provider} from "react-redux";
import {createStore, compose, applyMiddleware} from "redux";
import GameContainer from "./components/view/ViewRouter";
import rootReducer from "./components/RootReducer";
import gameService from "./middleware/gameService";
import pieceConfigService from "./middleware/pieceConfigService";

const composedEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const middleware = [gameService, pieceConfigService];

const store = createStore(
    rootReducer,
    composedEnhancer(applyMiddleware(...middleware))
);

const App = () => (
    <Provider store={store}>
        <GameContainer/>
    </Provider>
);

export default App;

