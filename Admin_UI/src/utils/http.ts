import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { log } from 'console';

class Http {
    private readonly http: AxiosInstance;

    constructor() {
        this.http = axios.create({
            baseURL: '/api', // 填写接口的基本路径
            timeout: 10000, // 设置请求超时时间,  
            withCredentials: true, // 允许发送 cookie
            crossDomain: true, // 设置为跨域请求    
        });

        // 请求拦截器
        this.http.interceptors.request.use(
            (config: AxiosRequestConfig) => {
                // 在请求发送之前，可以在这里对 config 做一些处理，比如添加请求头
                if (this.token) {
                    config.headers['token'] = this.token;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );

        // 响应拦截器
        this.http.interceptors.response.use(
            (response: AxiosResponse) => {
                const data=response.data
                
                // 在响应返回之前，可以在这里对 response 做一些处理，比如解析响应数据           
                if (data.Result) {
                    return response; // 如果 Result 为 true，则返回数据 ; return response axios默认返回response.data
                } 
                else {
                    return Promise.reject(new Error(data.ErrorMsg || '请求失败')); // 否则返回 Promise.reject
                }
            },
            (error) => {
                return Promise.reject(error);
            }
        );
    }

    // get 是一个类属性访问器（accessor），它用于获取该属性的值。
    // 访问 this.token 时，实际上是在调用 get token() 方法，该方法会返回本地存储中的 token 值。
    private get token(): string | null {
        // 这里返回本地存储中的 token，如果不存在则返回 null
        return localStorage.getItem('token');
    }

    public async get(url: string, params?: any, config?: AxiosRequestConfig): Promise<any> {
        const response = await this.http.get(url, { params, ...config });
        return response.data;
      }
      

    public async post(url: string, data?: any, config?: AxiosRequestConfig): Promise<any> {
        const response = await this.http.post(url, data, config);
        return response.data;
    }

    // ...
}

// 导出一个全局的实例
export default new Http();
