import postRequest from "@/services/postRequest";
import getRequest from "@/services/getRequest";

export async function loginUser(action) {
    console.log("sending request to login");
    return postRequest('/api/user/login', {}, action);
}

export async function getUserInfo(userId) {
    return getRequest('/api/user/user-info?userId='+userId);
}

export async function updateUser(userId, bio, selectedTags, selectedSkills, action) {
    return postRequest('/api/user/update?userId=' + userId,
        {'bio': bio, 'selectedTags': selectedTags,
            'selectedSkills': selectedSkills}, action);
}