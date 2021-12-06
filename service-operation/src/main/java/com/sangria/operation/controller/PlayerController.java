package com.sangria.operation.controller;

import com.sangria.operation.Enum.PlayerTradeTypeEnum;
import com.sangria.operation.dto.*;
import com.sangria.operation.service.PlayerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

@Api(tags = "Player Management Module")
@RestController
@RequestMapping("/player")
public class PlayerController extends BaseController{

    @Autowired
    PlayerService playerService;
    
    
    /**
     * add a player
     */
    @ApiOperation(value = "Add a new player using items in the specified inventory of the game")
    @PostMapping(value = "/add")
    public ResponseDTO add(@RequestBody PlayerAddDTO dto) {
    	if(StringUtils.isBlank(dto.getToken())) {
    		return renderFail("ERROR: authentication failed, token can not be empty");
    	}
    	if(StringUtils.isBlank(dto.getInventoryUuid())) {
    		return renderFail("ERROR: inventoryUuid can not be empty");
    	}
    	
    	return playerService.add(dto);
    }
    /**
     * get a player's info
     */
    @ApiOperation(value = "query a player's info")
    @GetMapping(value = "/info")
    public ResponseDTO info(String playerUuid, String token) {
    	if(StringUtils.isBlank(token)) {
    		return renderFail("ERROR: authentication failed, token can not be empty");
    	}
    	if(StringUtils.isBlank(playerUuid)) {
    		return renderFail("ERROR: playerUuid can not be empty");
    	}
    	
    	return playerService.info(playerUuid, token);
    }

    /**
     * freeze a player
     * @param dto
     * @return
     */
    @PostMapping(value = "/freeze")
    public ResponseDTO freeze(@RequestBody PlayerFreezeDTO dto) {
        String token = dto.getToken();
        String playerId = dto.getPlayerId();
        if(StringUtils.isBlank(token)) {
            return renderFail("ERROR: authentication failed, token can not be empty");
        }
        if(StringUtils.isBlank(playerId)) {
            return renderFail("ERROR: playerId can not be empty");
        }

        return playerService.freeze(dto);
    }

    /**
     * unfreeze a player
     * @param dto
     * @return
     */
    @PostMapping(value = "/unfreeze")
    public ResponseDTO unfreeze(@RequestBody PlayerUnfreezeDTO dto) {
        String token = dto.getToken();
        String playerId = dto.getPlayerId();
        if(StringUtils.isBlank(token)) {
            return renderFail("ERROR: authentication failed, token can not be empty");
        }
        if(StringUtils.isBlank(playerId)) {
            return renderFail("ERROR: playerId can not be empty");
        }

        return playerService.unfreeze(dto);
    }

    /**
	 * delete a player
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/deletePlayer")
	public ResponseDTO deletePlayer(@RequestBody PlayerDeleteDTO dto) {
		String token = dto.getToken();
		String inventoryId = dto.getPlayerId();
		if(StringUtils.isBlank(token)) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(inventoryId)) {
			return renderFail("ERROR: inventoryId can not be empty");
		}

		return playerService.deletePlayer(dto);
	}
	/**
	 * trade between players
	 */
	@ApiOperation(value = "handle 4 types of trades between 2 players")
	@PostMapping(value = "/trade")
	public ResponseDTO trade(@RequestBody PlayerTradeRequestDTO dto) {
		if(StringUtils.isBlank(dto.getToken())) {
    		return renderFail("ERROR: authentication failed, token can not be empty");
    	}
		if(dto.getType() == null) {
			return renderFail("ERROR: trade type can not be empty");
		}
		if(dto.getType() == PlayerTradeTypeEnum.MONEY_TO_MONEY.getType()) {
			if(dto.getPlayer1Amount() == null) {
				return renderFail("ERROR: player 1 trade amount can not be empty");
			}
			if(dto.getPlayer2Amount() == null) {
				return renderFail("ERROR: player 2 trade amount can not be empty");
			}
			if(dto.getPlayer1Amount() == 0 && dto.getPlayer2Amount() == 0) {
				return renderOk("trade completed");
			}
		}else if(dto.getType() == PlayerTradeTypeEnum.MONEY_TO_ITEM.getType()) {
			if(dto.getPlayer1Amount() == null) {
				return renderFail("ERROR: player 1 trade amount can not be empty");
			}
			if(dto.getPlayer2ItemList() == null || dto.getPlayer2ItemList().size() == 0) {
				return renderFail("ERROR: player 2 trade item list can not be empty");
			}
		}else if(dto.getType() == PlayerTradeTypeEnum.ITEM_TO_MONEY.getType()) {
			if(dto.getPlayer2Amount() == null) {
				return renderFail("ERROR: player 2 trade amount can not be empty");
			}
			if(dto.getPlayer1ItemList() == null || dto.getPlayer1ItemList().size() == 0) {
				return renderFail("ERROR: player 1 trade item list can not be empty");
			}
		}else if(dto.getType() == PlayerTradeTypeEnum.ITEM_TO_ITEM.getType()) {
			if(dto.getPlayer1ItemList() == null || dto.getPlayer1ItemList().size() == 0) {
				return renderFail("ERROR: player 1 trade item list can not be empty");
			}
			if(dto.getPlayer2ItemList() == null || dto.getPlayer2ItemList().size() == 0) {
				return renderFail("ERROR: player 2 trade item list can not be empty");
			}
		}else {
			return renderFail("ERROR: Invalid trade type");
		}
		
		return playerService.trade(dto);
	}

}
