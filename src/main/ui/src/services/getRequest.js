import {authorizationHeaders} from "@/authUtils";

export default async function getRequest(url) {
    const response = await fetch(url, {method: 'GET', headers: await authorizationHeaders()});
    if (response.status === 200) {
        let respJson = response.json();
        console.log(respJson);
        return respJson;
    }
    else {
        response.text().then(text => { alert(text); console.error(text); });
        return undefined;
    }
}