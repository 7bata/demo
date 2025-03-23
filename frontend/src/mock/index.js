// 模拟数据
const mockData = {
  // 课时统计数据
  courseHoursStats: {
    remainingHours: 20,
    consumedHours: 10
  },
  
  // 课时历史记录
  courseHoursHistory: {
    records: [
      {
        id: 1,
        date: '2023-05-01',
        subject: '数学',
        teacher: '张老师',
        hours: 2,
        report: '认真听讲，完成作业'
      },
      {
        id: 2,
        date: '2023-05-03',
        subject: '英语',
        teacher: '李老师',
        hours: 1.5,
        report: '积极参与课堂讨论'
      },
      {
        id: 3,
        date: '2023-05-05',
        subject: '物理',
        teacher: '王老师',
        hours: 2,
        report: '实验操作熟练'
      },
      {
        id: 4,
        date: '2023-05-08',
        subject: '化学',
        teacher: '赵老师',
        hours: 1.5,
        report: '认真完成实验'
      },
      {
        id: 5,
        date: '2023-05-10',
        subject: '生物',
        teacher: '钱老师',
        hours: 2,
        report: '课堂表现良好'
      }
    ],
    total: 5
  },
  
  // 课程表数据
  courseSchedule: [
    {
      id: 1,
      date: '2023-05-15',
      name: '高等数学',
      teacher: '张教授',
      location: '教学楼A101',
      startTime: '08:00',
      endTime: '09:30',
      type: 1
    },
    {
      id: 2,
      date: '2023-05-15',
      name: '大学英语',
      teacher: '李教授',
      location: '教学楼B203',
      startTime: '10:00',
      endTime: '11:30',
      type: 2
    },
    {
      id: 3,
      date: '2023-05-16',
      name: '大学物理',
      teacher: '王教授',
      location: '实验楼C305',
      startTime: '14:00',
      endTime: '15:30',
      type: 3
    },
    {
      id: 4,
      date: '2023-05-17',
      name: '程序设计',
      teacher: '刘教授',
      location: '计算机楼D401',
      startTime: '08:00',
      endTime: '09:30',
      type: 4
    },
    {
      id: 5,
      date: '2023-05-18',
      name: '数据结构',
      teacher: '陈教授',
      location: '计算机楼D402',
      startTime: '10:00',
      endTime: '11:30',
      type: 5
    },
    {
      id: 6,
      date: '2023-05-19',
      name: '操作系统',
      teacher: '张教授',
      location: '计算机楼D403',
      startTime: '14:00',
      endTime: '15:30',
      type: 1
    }
  ]
};

export default mockData;