import React, {Component} from "react";
import PropTypes from "prop-types";

class Spinner extends Component {
    static propTypes = {
        loadingImage: PropTypes.string,
        name: PropTypes.string
    };

    render() {
        const {loadingImage, name} = this.props;

        return <div style={spinnerStyle}>
            <img src={"https://acegif.com/wp-content/uploads/loading-1-gap.jpg"} alt={`Loading Spinner ${name}}`}/>
        </div>
    }
}

const spinnerStyle = {
    display: 'inline-block'
};


export default Spinner;