import React, { FC, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Typography, Box, Tabs, Tab } from '@mui/material'
import dayjs from 'dayjs'
import 'dayjs/locale/ru'
import { stations } from 'сonstants/stations'
import Meteoparams from './components/Meteoparams'
import Systemparams from './components/Systemparams'

dayjs.locale('ru')
// eslint-disable-next-line @typescript-eslint/ban-types
type Props = {}

enum PARAMETER_TYPE {
  METEO = 'meteo',
  SYSTEM = 'system',
}

type TabPanelProps = {
  // eslint-disable-next-line react/require-default-props
  children?: React.ReactNode
  type: PARAMETER_TYPE
  value: PARAMETER_TYPE
}

function TabPanel(props: TabPanelProps): JSX.Element {
  const { children, value, type, ...other } = props

  return (
    <div
      role="tabpanel"
      hidden={value !== type}
      id={`tabpanel-${type}`}
      aria-labelledby={`tab-${type}`}
      {...other}
    >
      {value === type && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  )
}

const Main: FC<Props> = (): JSX.Element => {
  const [activeTab, setActiveTab] = useState(PARAMETER_TYPE.METEO)
  const { stationId } = useParams()

  const handleChange = (
    event: React.SyntheticEvent,
    value: PARAMETER_TYPE
  ): void => {
    setActiveTab(value)
  }

  return (
    <div>
      <Typography variant="h4" gutterBottom component="h2">
        {stations[stationId || ''].name}
      </Typography>

      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs
          value={activeTab}
          onChange={handleChange}
          aria-label="parameter-type"
        >
          <Tab label="Метеопараметры" value={PARAMETER_TYPE.METEO} />
          <Tab label="Системные параметры" value={PARAMETER_TYPE.SYSTEM} />
        </Tabs>
      </Box>
      <TabPanel value={activeTab} type={PARAMETER_TYPE.METEO}>
        <Meteoparams />
      </TabPanel>
      <TabPanel value={activeTab} type={PARAMETER_TYPE.SYSTEM}>
        <Systemparams />
      </TabPanel>
    </div>
  )
}

export default Main
