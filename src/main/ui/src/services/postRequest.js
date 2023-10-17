import {whenTokenNotUndefined} from "@/authUtils";
import axios from "axios";

export default function postRequest(url, action) {
    whenTokenNotUndefined().then( () => {
        axios({
            method: 'post',
            url: url,
            headers: {'Authorization': "Bearer " + localStorage.getItem('user-token')}
        }).then((response) => {console.log(response); action(response);}).catch((error) => {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.error(error.response.data);
                console.error(error.response.status);
                console.error(error.response.headers);
            } else if (error.request) {
                // The request was made but no response was received
                // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                // http.ClientRequest in node.js
                console.error("The request was made but no response was received ", error.request);
            } else {
                // Something happened in setting up the request that triggered an Error
                console.error(error.message);
            }
            console.log(error.config);
        });
    });
}