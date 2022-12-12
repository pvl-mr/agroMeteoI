/* eslint-disable react/no-unused-prop-types */
import { FC, useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import dayjs, { ManipulateType } from 'dayjs'
import 'dayjs/locale/ru'

import LabelCheckbox from 'components/LabelCheckbox'
import DatetimePicker from 'components/DatetimePicker'

import { Button } from '@mui/material'
import { Parameters, Period, MeteoBlock } from '../styles'
import ItemMeteoParam from './ItemMeteoParam'

dayjs.locale('ru')
// eslint-disable-next-line @typescript-eslint/ban-types
type Props = {}

const DEFAULT_METEO_PARAMS: Record<string, any> = {
  SOLAR_RADIATION: {
    label: 'Солнечная радиация',
    enabled: false,
  },
  PRECIPITATION: {
    label: 'Осадки',
    enabled: false,
  },
  WIND_SPEED: {
    label: 'Скорость ветра',
    enabled: false,
  },
  LEAF_WETNESS: {
    label: 'Влажность листа',
    enabled: false,
  },
  HC_AIR_TEMPERATURE: {
    label: 'Температура воздуха',
    enabled: false,
  },
  HC_RELATIVE_HUMIDITY: {
    label: 'Влажность воздуха',
    enabled: false,
  },
  DEW_POINT: {
    label: 'Точка росы',
    enabled: false,
  },
}

enum DATE_FILTER {
  START = 'start',
  END = 'end',
}

type DateRangeType = {
  label: string
  value: ManipulateType
}

const DATE_RANGE: DateRangeType[] = [
  {
    label: 'За сутки',
    value: 'days',
  },
  {
    label: 'За неделю',
    value: 'weeks',
  },
]

const timestampToString = (timestamp: number | null): string | null => {
  if (!timestamp) {
    return null
  }

  return dayjs.unix(timestamp).format('YYYY-MM-DDTHH:mm')
}

const Meteoparams: FC<Props> = (): JSX.Element => {
  const [meteoParams, setMeteoParams] = useState<Record<any, any>>(
    DEFAULT_METEO_PARAMS
  )
  const [startTime, setStartTime] = useState<number>(
    dayjs().subtract(1, 'month').unix()
  )
  const [endTime, setEndTime] = useState<number>(dayjs().unix())
  const { stationId } = useParams()

  const handleChangeDate = (e: any): void => {
    const { value, name } = e.target
    const timestamp = dayjs(value).unix()
    if (name === DATE_FILTER.START) {
      setStartTime(timestamp)
      return
    }
    setEndTime(timestamp)
  }

  const handleChangeParams = (e: any): void => {
    const { name } = e.target
    setMeteoParams((prev) => ({
      ...prev,
      [name]: {
        ...prev[name],
        enabled: !prev[name].enabled,
      },
    }))
  }

  const setRange = (value: ManipulateType): void => {
    const start = dayjs().subtract(1, value).unix()
    const end = dayjs().unix()
    setStartTime(start)
    setEndTime(end)
  }

  return (
    <MeteoBlock>
      <Parameters>
        {Object.entries(meteoParams).map(([key, { label, enabled }]) => (
          <LabelCheckbox
            key={key}
            label={label}
            checked={enabled}
            name={key}
            onChange={handleChangeParams}
          />
        ))}
      </Parameters>
      <Period>
        <DatetimePicker
          label="От"
          value={timestampToString(startTime)}
          onChange={handleChangeDate}
          name={DATE_FILTER.START}
        />
        <DatetimePicker
          label="До"
          value={timestampToString(endTime)}
          onChange={handleChangeDate}
          name={DATE_FILTER.END}
        />
        {DATE_RANGE.map(({ label, value }: DateRangeType) => (
          <Button onClick={() => setRange(value)} variant="outlined">
            {label}
          </Button>
        ))}
      </Period>
      {Object.entries(meteoParams).map(
        ([key, value]) =>
          value.enabled && (
            <ItemMeteoParam
              startTime={startTime}
              endTime={endTime}
              key={key}
              parameterName={key}
              label={value.label}
            />
          )
      )}
    </MeteoBlock>
  )
}

export default Meteoparams
