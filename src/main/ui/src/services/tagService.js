import getRequest from "@/services/getRequest";

export async function getTags() {
    return getRequest('/api/tags/get');
}