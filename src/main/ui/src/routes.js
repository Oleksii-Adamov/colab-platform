import TestPage from "@/pages/TestPage";
import LandingPage from "@/pages/LandingPage";
import ProjectPage from "@/pages/ProjectPage";
import MyProjectsPage from "@/pages/MyProjectsPage";


export default [
    {
        path: '/',
        component: LandingPage
    },
    {
        path: '/test',
        component: TestPage
    },
    {
        path: '/my-projects',
        component: MyProjectsPage
    },
    {
        path: '/projects/:id',
        name: 'project-page',
        component: ProjectPage,
        props: true,
    },
    {
        path: '/:unused*',
        redirect: '/',
    }
]