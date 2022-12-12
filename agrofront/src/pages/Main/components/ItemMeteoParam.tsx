import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import axios from 'axios'
import { Box, Card, CircularProgress } from '@mui/material'
import dayjs from 'dayjs'
import 'dayjs/locale/ru'
import styled from 'styled-components'

import Chart from 'components/Chart'
import LabelCheckbox from 'components/LabelCheckbox'
import { BASE_URL } from 'сonstants/static'
import endpoints from 'сonstants/endpoints'

dayjs.locale('ru')

const StyledCard = styled(Card)`
  padding: 24px;
  position: relative;
`

const WrapperLoader = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  z-index: 2;
`

const DEFAULT_ADDITIONAL_SETTINGS = {
  PRECIPITATION: {
    [endpoints.GET_INCREASED_PRECIPITATION]: {
      enabled: false,
      label: 'Нарастающая',
      borderColor: 'rgb(245, 40, 145)',
      backgroundColor: 'rgba(245, 40, 145, 0.5)',
    },
    [endpoints.GET_NORM_PRECIPITATION]: {
      enabled: false,
      label: 'Норма для региона',
      borderColor: 'rgb(0, 191, 104)',
      backgroundColor: 'rgba(0, 191, 104, 0.5)',
    },
  },
  HC_AIR_TEMPERATURE: {
    [endpoints.GET_NORM_TEMPERATURE]: {
      enabled: false,
      label: 'Норма для региона',
      borderColor: 'rgb(0, 191, 104)',
      backgroundColor: 'rgba(0, 191, 104, 0.5)',
    },
  },
}
// eslint-disable-next-line @typescript-eslint/ban-types
type Props = {
  startTime: number
  endTime: number
  label: string
  parameterName: string
}

const ItemMeteoParam: FC<Props> = ({
  startTime,
  endTime,
  label,
  parameterName,
}): JSX.Element | null => {
  const [additionalSettings, setAdditionalSettings] = useState<any>(
    DEFAULT_ADDITIONAL_SETTINGS
  )
  const [additionalData, setAdditionalData] = useState<any[]>([])
  const [meteoData, setMeteoData] = useState<string[] | number[]>([])
  const [labels, setLabels] = useState<string[] | null>(null)
  const [isLoading, setIsLoading] = useState<boolean>(false)
  const [isLoadingAddtitional, setIsLoadingAddtitional] = useState<boolean>(
    false
  )
  const { stationId } = useParams()

  const getData = async (): Promise<any> => {
    try {
      setIsLoading(true)
      const { data } = await axios(`${BASE_URL}${endpoints.GET_INFO}`, {
        method: 'POST',
        data: {
          meteoId: stationId,
          startTime,
          endTime,
          parameterName,
        },
      })

      setMeteoData(data.values.values)
      setLabels(data.dates)
    } catch (err: any) {
      setMeteoData([])
      setLabels(null)
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    getData()
  }, [startTime, endTime, stationId])

  const getAdditionalData = async (settingsKey: string): Promise<any> => {
    try {
      setIsLoadingAddtitional(true)
      // eslint-disable-next-line @typescript-eslint/no-shadow
      const { label, borderColor, backgroundColor } = additionalSettings[
        parameterName
      ][settingsKey]
      const { data } = await axios(`${BASE_URL}${settingsKey}`, {
        method: 'POST',
        data: {
          meteoId: stationId,
          startTime,
          endTime,
          parameterName,
        },
      })

      setAdditionalData([
        ...additionalData,
        {
          data: data.values.values,
          label,
          borderColor,
          backgroundColor,
          key: settingsKey,
        },
      ])
    } catch (err: any) {
      setAdditionalData((prev: any) =>
        prev.filter((item: any) => item.key !== settingsKey)
      )
    } finally {
      setIsLoadingAddtitional(false)
    }
  }

  const onChangeAdditionalSettings = async (
    e: any,
    settingsKey: string
  ): Promise<any> => {
    const { checked } = e.target
    setAdditionalSettings((prev: any) => ({
      ...prev,
      [parameterName]: {
        ...prev[parameterName],
        [settingsKey]: {
          ...prev[parameterName][settingsKey],
          enabled: checked,
        },
      },
    }))

    if (checked) {
      getAdditionalData(settingsKey)
    } else {
      setAdditionalData((prev: any) =>
        prev.filter((item: any) => item.key !== settingsKey)
      )
    }
  }

  if (isLoading) {
    return (
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
        <CircularProgress />
      </Box>
    )
  }

  if (!labels) {
    return null
  }

  return (
    <StyledCard>
      {isLoadingAddtitional && (
        <WrapperLoader>
          <CircularProgress />
        </WrapperLoader>
      )}
      {/* eslint-disable-next-line no-prototype-builtins */}
      {additionalSettings.hasOwnProperty(parameterName) &&
        Object.entries(
          additionalSettings[parameterName]
        ).map(([key, value]: any) => (
          <LabelCheckbox
            key={`${parameterName}-${key}}`}
            label={value.label}
            checked={value.enabled}
            onChange={(e) => onChangeAdditionalSettings(e, key)}
          />
        ))}
      <Chart
        parameterName={parameterName}
        labels={labels}
        datasets={[
          ...additionalData,
          {
            data: meteoData,
            label,
          },
        ]}
      />
    </StyledCard>
  )
}

export default ItemMeteoParam
