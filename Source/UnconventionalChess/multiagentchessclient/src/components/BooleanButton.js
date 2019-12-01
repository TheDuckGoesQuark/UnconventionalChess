import React from "react";

const BooleanButton = ({trueText, falseText, callback, isTrue, disabled}) => (
    <div>
        <button type="button" disabled={disabled}
                style={isTrue ? chosenButton : null}
                onClick={() => callback(true)}>
            {trueText}
        </button>
        <button type="button" disabled={disabled}
                style={!isTrue ? chosenButton : null}
                onClick={() => callback(false)}>
            {falseText}
        </button>
    </div>
);

const chosenButton = {
    backgroundColor: "green"
};

export default BooleanButton;
