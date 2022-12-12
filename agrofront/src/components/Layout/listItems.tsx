import React from 'react'

import { ListItem, ListItemIcon, ListItemText } from '@mui/material'
import { useNavigate, generatePath } from 'react-router-dom'

import { stations } from 'сonstants/stations'
import paths from 'сonstants/paths'

export default function MainListItems(): JSX.Element {
  const navigate = useNavigate()

  // const openPage = (station: string): void => {
  //   window.location.pathname = `/${stationId}`
  // }

  const pathUrl = (stationId: string): string =>
    generatePath(paths.MAIN, {
      stationId,
    })
  return (
    <div>
      {Object.entries(stations).map(([stationId, stationValue]) => (
        <ListItem
          key={stationId}
          button
          onClick={() => navigate(pathUrl(stationId))}
        >
          <ListItemIcon>{stationValue.icon}</ListItemIcon>
          <ListItemText primary={stationValue.name} />
        </ListItem>
      ))}
    </div>
  )
}
