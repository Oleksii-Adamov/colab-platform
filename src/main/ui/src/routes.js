import TestPage from "@/pages/TestPage";
import LandingPage from "@/pages/LandingPage";
import ProjectPage from "@/pages/ProjectPage";
import MyProjectsPage from "@/pages/MyProjectsPage";
import CreateProjectPage from "@/pages/CreateProjectPage";
import ProjectSearchPage from "@/pages/ProjectSearchPage";


export default [
    {
        path: '/test',
        component: TestPage
    },
    {
        path: '/create-project',
        component: CreateProjectPage,
        name: 'create-project-page'
    },
    {
        path: '/my-projects',
        component: MyProjectsPage,
        name: 'my-projects-page'
    },
    {
        path: '/project-search',
        component: ProjectSearchPage,
        name: 'project-search-page'
    },
    {
        path: '/projects/:id',
        name: 'project-page',
        component: ProjectPage,
        props: true,
    },
    {
        path: '/',
        component: LandingPage
    },
    {
        path: '/:unused*',
        redirect: '/',
    }
]