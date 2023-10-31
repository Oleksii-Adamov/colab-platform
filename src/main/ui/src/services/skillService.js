import getRequest from "@/services/getRequest";

export async function getSkills() {
    return getRequest('/api/skills/get');
}