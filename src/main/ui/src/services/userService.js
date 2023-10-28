import postRequest from "@/services/postRequest";

export async function loginUser() {
    return postRequest('/api/user/login');
}