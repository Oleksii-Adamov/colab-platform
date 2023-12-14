import getRequest from "@/services/getRequest";

export async function getStats(beginningMonth, beginningYear, endMonth, endYear) {
    return getRequest('/api/admin/stats?beginningMonth=' + beginningMonth + '&beginningYear='+ beginningYear +
        '&endMonth='+ endMonth + '&endYear='+ endYear);
}