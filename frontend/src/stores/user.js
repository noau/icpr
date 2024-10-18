import {defineStore} from 'pinia'
import {ref} from 'vue'
import {userGetInfoService} from '@/api/user.js'
// 用户模块
export const useUserStore = defineStore(
    'big-user',
    () => {
        const token = ref('')
        const id = ref('')
        const course = ref('')
        const setToken = (newToken) => {
            token.value = newToken
        }
        const setId = (newId) => {
            console.log(newId)
            id.value = newId
        }
        const getId = () => id.value
        const removeToken = () => {
            token.value = ''
        }
        const setCourse = (newCourse) => {
            course.value = newCourse
        }
        const getCourse = () => course.value
        const removeCourse = () => {
            course.value = ''
        }

        const user = ref({})
        const getUser = async () => {
            const res = await userGetInfoService()
            user.value = res.data.data
        }
        const setUser = (obj) => {
            user.value = obj
        }
        return {course, token, id, setToken, removeToken, setId, getId, setCourse, getCourse, removeCourse, user, getUser, setUser}
    },
    {
        persist: true
    }
)
