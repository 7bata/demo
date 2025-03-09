import axios from 'axios';
import {ElMessage} from "element-plus";

const defaultError = () => ElMessage.error('请求失败，请稍后再试，或联系管理员！');
const defaultFailure = (message) => ElMessage.error(message);

//封装post请求
function post(url, success, data, failure = defaultFailure, error = defaultError){
    axios.post(url, data,{
        headers: {//设置请求头
            'Content-Type' : 'application/x-www-form-urlencoded'
        },
        withCredentials: true//是否携带cookie
    }).then(({data}) => {
        if(data.success){
            success(data.message, data.status);
        }
        else{
            failure(data.message, data.status);
        }
    }).catch(error)

}
//封装get请求
function get(url, success, failure = defaultFailure, error = defaultError){
    axios.post(url, {
        withCredentials: true//是否携带cookie
    }).then(({data}) => {
        if(data.success){
            success(data.message, data.status);
        }
        else{
            failure(data.message, data.status);
        }
    }).catch(error)
}
//暴漏接口
export{
    post,
    get
}