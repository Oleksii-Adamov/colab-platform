import postRequest from "@/services/postRequest";

export async function loginUser(action) {
    console.log("sending request to login");
    return postRequest('/api/user/login', action);
}