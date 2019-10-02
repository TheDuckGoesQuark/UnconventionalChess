import React from "react";
import {Provider} from "react-redux";
import {createStore} from "redux";
import GameContainer from "./components/view/ViewRouter";
import rootReducer from "./components/rootReducer";

const store = createStore(rootReducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());

const App = () => (
    <Provider store={store}>
        <GameContainer/>
    </Provider>
);

export default App;

