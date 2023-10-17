import getRequest from "@/services/getRequest";

export async function getTestItems() {
    return getRequest('/api/test');
}