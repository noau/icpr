import {defineStore} from 'pinia'
import {ref} from 'vue'
import {userGetInfoService} from '@/api/user.js'
// 用户模块
export const useUserStore = defineStore(
    'big-user',
    () => {
        const token = ref('')
        const id = ref('')
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

        const user = ref({})
        const getUser = async () => {
            const res = await userGetInfoService()
            user.value = res.data.data
        }
        const setUser = (obj) => {
            user.value = obj
        }
        return {token, id, setToken, removeToken, setId, getId, user, getUser, setUser}
    },
    {
        persist: true
    }
)
