package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.MainComponentBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.home.R
import com.wiseduck.squadbuilder.feature.home.TeamSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSortDropdown(
    modifier: Modifier = Modifier,
    currentSortOption: TeamSortOption,
    onSortOptionSelected: (TeamSortOption) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedText =
        when (currentSortOption) {
            TeamSortOption.LATEST -> stringResource(R.string.team_sort_option_1)
            TeamSortOption.NAME -> stringResource(R.string.team_sort_option_2)
        }

    val customTextFieldColors =
        ExposedDropdownMenuDefaults.outlinedTextFieldColors(
            focusedContainerColor = MainComponentBg,
            unfocusedContainerColor = MainComponentBg,
            focusedTextColor = Neutral300,
            unfocusedTextColor = Neutral300,
            unfocusedLabelColor = Neutral300,
            focusedTrailingIconColor = Neutral300,
            unfocusedTrailingIconColor = Neutral50,
            focusedBorderColor = Neutral800,
            unfocusedBorderColor = Neutral800,
        )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth(0.5f),
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = selectedText,
            onValueChange = {},
            label = {
                stringResource(R.string.team_sort_label)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = customTextFieldColors,
            shape = RoundedCornerShape(
                SquadBuilderTheme.radius.md,
            ),
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .background(MainComponentBg)
                .exposedDropdownSize(true),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            TeamSortOption.entries.forEach { selectionOption ->
                val optionText =
                    when (selectionOption) {
                        TeamSortOption.LATEST -> stringResource(R.string.team_sort_option_1)
                        TeamSortOption.NAME -> stringResource(R.string.team_sort_option_2)
                    }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Neutral50,
                        )
                    },
                    onClick = {
                        onSortOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun TeamSortDropdownPreview() {
    SquadBuilderTheme {
        TeamSortDropdown(
            currentSortOption = TeamSortOption.LATEST,
            onSortOptionSelected = { },
        )
    }
}
