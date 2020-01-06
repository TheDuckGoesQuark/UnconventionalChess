import {
    fetchPersonalityTypesError,
    fetchPersonalities,
    fetchPersonalityTypesSuccess,
    CONFIG_FETCH_PERSONALITIES_SUCCESS,
    CONFIG_FETCH_PERSONALITIES_ERROR,
    CONFIG_FETCH_PERSONALITIES
} from "../components/config/ConfigActions";


const fetchPersonalityTypes = (dispatch) => {
    fetch('/personalities', {
        headers: {
            'Accept': 'application/json',
        },
    }).then(res => {
        return res.json()
    }).then(res => {
        if (res.error) {
            throw(res.error);
        } else {
            dispatch(fetchPersonalityTypesSuccess(res.personalityTypes));
            return res.personalityTypes;
        }
    }).catch(error => {
        dispatch(fetchPersonalityTypesError(error))
    });
};

const personalityService = store => next => action => {
    next(action);

    switch (action.type) {
        case CONFIG_FETCH_PERSONALITIES:
            fetchPersonalityTypes(next);
            break;
        default:
            break;
    }
};

export default personalityService;