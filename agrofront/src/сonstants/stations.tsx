import {
  Dashboard as DashboardIcon,
  Layers as LayersIcon,
  Assignment as AssignmentIcon,
} from '@mui/icons-material'

export type StationValueProps = {
  name: string
  icon: any
}

export const DEFAULT_STATION = '00001F76'

export const stations: Record<string, StationValueProps> = {
  '00001F76': {
    name: 'Сервисный центр',
    icon: <DashboardIcon />,
  },
  '00001F77': {
    name: 'Отделение 17',
    icon: <AssignmentIcon />,
  },
  '00001F78': {
    name: 'Отделение 9',
    icon: <AssignmentIcon />,
  },
  '00001F7D': {
    name: 'Отделение 12',
    icon: <AssignmentIcon />,
  },
  '0000235D': {
    name: 'ПУ Север',
    icon: <LayersIcon />,
  },
  '0000235E': {
    name: 'ПУ Кавказ',
    icon: <LayersIcon />,
  },
}
